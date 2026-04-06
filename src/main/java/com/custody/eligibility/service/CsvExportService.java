package com.custody.eligibility.service;

import com.custody.eligibility.model.EligibilityResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Service
public class CsvExportService {

    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
    private static final DecimalFormat PERCENTAGE_FORMAT = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
    private static final DecimalFormat QUANTITY_FORMAT = new DecimalFormat("#,##0", new DecimalFormatSymbols(Locale.US));

    public String generateEligibilityResultCsv(EligibilityResult result) {
        StringBuilder csv = new StringBuilder();
        
        // Header
        csv.append("Dividend Eligibility Result\n");
        csv.append("Client:,").append(result.getClientName()).append("\n");
        csv.append("Declaration ID:,").append(result.getDeclarationId()).append("\n");
        csv.append("ISIN:,").append(result.getIsin()).append("\n");
        csv.append("Issuer:,").append(result.getIssuerName()).append("\n");
        csv.append("Eligibility Status:,").append(result.getEligibilityStatus()).append("\n\n");
        
        // Position Summary
        csv.append("Position Summary\n");
        csv.append("Settled Quantity,").append(QUANTITY_FORMAT.format(result.getSettledQuantity())).append("\n");
        csv.append("On Loan Quantity,").append(QUANTITY_FORMAT.format(result.getOnLoanQuantity())).append("\n");
        csv.append("Pending Settlement Quantity,").append(QUANTITY_FORMAT.format(result.getPendingSettlementQuantity())).append("\n");
        csv.append("Eligible Quantity,").append(QUANTITY_FORMAT.format(result.getEligibleQuantity())).append("\n");
        csv.append("Claim Quantity,").append(QUANTITY_FORMAT.format(result.getClaimQuantity())).append("\n\n");
        
        // Real Dividend Entitlement
        csv.append("Real Dividend Entitlement\n");
        csv.append("Gross Real Dividend,").append(CURRENCY_FORMAT.format(result.getGrossRealDividend())).append("\n");
        csv.append("Withholding Tax Rate Applied,").append(formatPercentage(result.getWithholdingTaxRateApplied())).append("\n");
        csv.append("Withholding Tax Amount,").append(CURRENCY_FORMAT.format(result.getWithholdingTaxAmount())).append("\n");
        csv.append("Net Real Dividend,").append(CURRENCY_FORMAT.format(result.getNetRealDividend())).append("\n\n");
        
        // Manufactured Dividend Entitlement (if applicable)
        if (result.getOnLoanQuantity() > 0) {
            csv.append("Manufactured Dividend Entitlement\n");
            csv.append("Gross Manufactured Dividend,").append(CURRENCY_FORMAT.format(result.getGrossManufacturedDividend())).append("\n");
            csv.append("Statutory WHT Rate,").append(formatPercentage(result.getManufacturedWithholdingTaxRate())).append("\n");
            csv.append("Manufactured WHT Amount,").append(CURRENCY_FORMAT.format(result.getManufacturedWithholdingTaxAmount())).append("\n");
            csv.append("Net Manufactured Dividend (pre gross-up),").append(CURRENCY_FORMAT.format(result.getNetManufacturedDividend())).append("\n");
            csv.append("Gross-Up Amount,").append(CURRENCY_FORMAT.format(result.getGrossUpAmount())).append("\n");
            csv.append("Net Manufactured Dividend (post gross-up),").append(CURRENCY_FORMAT.format(result.getNetManufacturedDividendAfterGrossUp())).append("\n\n");
        }
        
        // Dividend Claim (if applicable)
        if (result.getClaimQuantity() > 0) {
            csv.append("Dividend Claim\n");
            csv.append("Claim Quantity,").append(QUANTITY_FORMAT.format(result.getClaimQuantity())).append("\n");
            csv.append("Gross Claim Amount,").append(CURRENCY_FORMAT.format(result.getDividendClaimAmount())).append("\n\n");
        }
        
        // Total Net Entitlement
        csv.append("Total Net Entitlement\n");
        csv.append("Total Net Entitlement,").append(CURRENCY_FORMAT.format(result.getTotalNetEntitlement())).append("\n\n");
        
        // Processing Notes
        csv.append("Processing Notes\n");
        for (int i = 0; i < result.getProcessingNotes().size(); i++) {
            csv.append(i + 1).append(".,").append(escapeCsv(result.getProcessingNotes().get(i))).append("\n");
        }
        
        return csv.toString();
    }
    
    private String formatPercentage(BigDecimal rate) {
        if (rate == null) return "0.00%";
        return PERCENTAGE_FORMAT.format(rate) + "%";
    }
    
    private String escapeCsv(String value) {
        if (value == null) return "";
        // Escape quotes and wrap in quotes if contains comma, quote, or newline
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return value.replace("\"", "\"\"").replaceAll("\n", "\\n");
        }
        return value;
    }
}
