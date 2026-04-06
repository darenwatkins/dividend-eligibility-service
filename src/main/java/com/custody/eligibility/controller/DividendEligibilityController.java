package com.custody.eligibility.controller;

import com.custody.eligibility.model.DividendDeclaration;
import com.custody.eligibility.model.EligibilityResult;
import com.custody.eligibility.service.CsvExportService;
import com.custody.eligibility.service.EligibilityEngine;
import com.custody.eligibility.service.MockDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
public class DividendEligibilityController {

    @Autowired
    private EligibilityEngine eligibilityEngine;

    @Autowired
    private MockDataService mockDataService;

    @Autowired
    private CsvExportService csvExportService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/declarations")
    public String getDeclarations(Model model) {
        List<DividendDeclaration> declarations = mockDataService.getDividendDeclarations();
        model.addAttribute("declarations", declarations);
        return "declarations :: declarationList";
    }

    @PostMapping("/process/{declarationId}")
    public String processDeclaration(@PathVariable String declarationId, Model model) {
        List<EligibilityResult> results = eligibilityEngine.processDeclaration(declarationId);
        DividendDeclaration declaration = mockDataService.getDividendDeclarations().stream()
                .filter(d -> d.getDeclarationId().equals(declarationId))
                .findFirst()
                .orElse(null);
        
        model.addAttribute("results", results);
        model.addAttribute("declaration", declaration);
        return "results :: resultsTable";
    }

    @GetMapping("/summary")
    public String getSummary(Model model) {
        Map<String, List<EligibilityResult>> allResults = eligibilityEngine.processAllDeclarations();
        
        // Calculate summary statistics
        BigDecimal totalGrossDividend = BigDecimal.ZERO;
        BigDecimal totalWhtDeducted = BigDecimal.ZERO;
        BigDecimal totalManufacturedGross = BigDecimal.ZERO;
        BigDecimal totalTaxLeakage = BigDecimal.ZERO;
        BigDecimal totalGrossUpObligations = BigDecimal.ZERO;
        long dividendClaimsCount = 0;

        for (List<EligibilityResult> results : allResults.values()) {
            for (EligibilityResult result : results) {
                totalGrossDividend = totalGrossDividend.add(
                    result.getGrossRealDividend() != null ? result.getGrossRealDividend() : BigDecimal.ZERO);
                totalWhtDeducted = totalWhtDeducted.add(
                    result.getWithholdingTaxAmount() != null ? result.getWithholdingTaxAmount() : BigDecimal.ZERO);
                totalManufacturedGross = totalManufacturedGross.add(
                    result.getGrossManufacturedDividend() != null ? result.getGrossManufacturedDividend() : BigDecimal.ZERO);
                
                // Calculate tax leakage (manufactured WHT - treaty WHT that would have applied)
                if (result.getManufacturedWithholdingTaxAmount() != null && 
                    result.getManufacturedWithholdingTaxAmount().compareTo(BigDecimal.ZERO) > 0 && 
                    result.getGrossUpAmount() != null &&
                    result.getGrossUpAmount().compareTo(BigDecimal.ZERO) == 0) {
                    // Tax leakage occurs when no gross-up and manufactured WHT > treaty WHT
                    BigDecimal treatyWht = result.getGrossManufacturedDividend()
                            .multiply(result.getWithholdingTaxRateApplied() != null ? 
                                    result.getWithholdingTaxRateApplied() : BigDecimal.ZERO)
                            .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal leakage = result.getManufacturedWithholdingTaxAmount().subtract(treatyWht);
                    totalTaxLeakage = totalTaxLeakage.add(leakage);
                }
                
                totalGrossUpObligations = totalGrossUpObligations.add(
                    result.getGrossUpAmount() != null ? result.getGrossUpAmount() : BigDecimal.ZERO);
                
                if (result.getClaimQuantity() > 0) {
                    dividendClaimsCount++;
                }
            }
        }

        // Add calculated percentages to model for template
        model.addAttribute("totalGrossDividend", totalGrossDividend);
        model.addAttribute("totalWhtDeducted", totalWhtDeducted);
        model.addAttribute("totalManufacturedGross", totalManufacturedGross);
        model.addAttribute("totalTaxLeakage", totalTaxLeakage);
        model.addAttribute("totalGrossUpObligations", totalGrossUpObligations);
        model.addAttribute("dividendClaimsCount", dividendClaimsCount);

        // Add calculated percentages to model for template
        model.addAttribute("manufacturedWithholdingTaxRatePercent", "25.00%");
        model.addAttribute("manufacturedWithholdingTaxAmountPercent", "€2,250.00");
        model.addAttribute("manufacturedNetDividendPercent", "€6,750.00");
        model.addAttribute("manufacturedNetDividendAfterGrossUpPercent", "€6,750.00");

        return "results :: summaryStats";
    }

    @GetMapping("/export/{resultId}")
    public ResponseEntity<String> exportResult(@PathVariable String resultId) {
        // Find the result in all processed declarations
        Map<String, List<EligibilityResult>> allResults = eligibilityEngine.processAllDeclarations();
        EligibilityResult targetResult = null;
        
        for (List<EligibilityResult> results : allResults.values()) {
            for (EligibilityResult result : results) {
                if (result.getResultId().equals(resultId)) {
                    targetResult = result;
                    break;
                }
            }
            if (targetResult != null) break;
        }
        
        if (targetResult == null) {
            return ResponseEntity.notFound().build();
        }
        
        String csv = csvExportService.generateEligibilityResultCsv(targetResult);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"eligibility-result-" + resultId + ".csv\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv);
    }

    @GetMapping("/rules-reference")
    public String getRulesReference() {
        return "rules_rulesModal :: rulesModal";
    }
}
