package com.custody.eligibility.service;

import com.custody.eligibility.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EligibilityEngine {

    @Autowired
    private MockDataService mockDataService;

    public List<EligibilityResult> processDeclaration(String declarationId) {
        List<EligibilityResult> results = new ArrayList<>();
        
        // Get the dividend declaration
        Optional<DividendDeclaration> declarationOpt = mockDataService.getDividendDeclarations().stream()
                .filter(d -> d.getDeclarationId().equals(declarationId))
                .findFirst();
        
        if (declarationOpt.isEmpty()) {
            return results;
        }
        
        DividendDeclaration declaration = declarationOpt.get();
        
        // Get all clients and their positions
        List<Client> clients = mockDataService.getClients();
        List<SecurityPosition> positions = mockDataService.getSecurityPositions();
        List<StockLoanPosition> loans = mockDataService.getStockLoanPositions();
        List<WithholdingTaxRule> taxRules = mockDataService.getWithholdingTaxRules();
        
        for (Client client : clients) {
            // Find position for this client
            Optional<SecurityPosition> positionOpt = positions.stream()
                    .filter(p -> p.getClientId().equals(client.getClientId()) && 
                               p.getIsin().equals(declaration.getIsin()))
                    .findFirst();
            
            if (positionOpt.isEmpty()) {
                continue;
            }
            
            SecurityPosition position = positionOpt.get();
            
            // Find loan position for this client
            Optional<StockLoanPosition> loanOpt = loans.stream()
                    .filter(l -> l.getLenderClientId().equals(client.getClientId()) && 
                               l.getIsin().equals(declaration.getIsin()))
                    .findFirst();
            
            // Find tax rule for this client
            Optional<WithholdingTaxRule> taxRuleOpt = taxRules.stream()
                    .filter(r -> r.getIsin().equals(declaration.getIsin()) &&
                               r.getClientType() == client.getClientType() &&
                               r.getClientDomicile().equals(client.getDomicile()))
                    .findFirst();
            
            EligibilityResult.EligibilityResultBuilder resultBuilder = EligibilityResult.builder()
                    .resultId(UUID.randomUUID().toString())
                    .clientId(client.getClientId())
                    .clientName(client.getClientName())
                    .declarationId(declaration.getDeclarationId())
                    .isin(declaration.getIsin())
                    .issuerName(declaration.getIssuerName())
                    .settledQuantity(position.getSettledQuantity())
                    .onLoanQuantity(position.getOnLoanQuantity())
                    .pendingSettlementQuantity(position.getPendingSettlementQuantity())
                    .processingNotes(new ArrayList<>());
            
            List<String> notes = resultBuilder.build().getProcessingNotes();
            
            // RULE 1 - ELIGIBLE QUANTITY
            long eligibleQuantity = position.getSettledQuantity() - position.getOnLoanQuantity();
            if (eligibleQuantity < 0) {
                eligibleQuantity = 0;
                notes.add("WARNING: Eligible quantity negative. Set to 0. Eligible quantity calculated as settled [" + 
                         position.getSettledQuantity() + "] minus on-loan [" + 
                         position.getOnLoanQuantity() + "] = " + eligibleQuantity);
            } else {
                notes.add("Eligible quantity calculated as settled [" + position.getSettledQuantity() + 
                         "] minus on-loan [" + position.getOnLoanQuantity() + "] = " + eligibleQuantity);
            }
            resultBuilder.eligibleQuantity(eligibleQuantity);
            
            // RULE 2 - CLAIM QUANTITY
            long claimQuantity = 0;
            if (position.getPendingSettlementQuantity() > 0) {
                claimQuantity = position.getPendingSettlementQuantity();
                notes.add("Cum-dividend pending buy of " + claimQuantity + 
                         " shares detected settling after record date. Dividend claim to be raised against selling counterparty.");
            } else if (position.getPendingSettlementQuantity() < 0) {
                notes.add("Pending sell of " + Math.abs(position.getPendingSettlementQuantity()) + 
                         " shares noted. These shares remain with lender at record date as settlement has not occurred; no adjustment to eligible quantity.");
            }
            resultBuilder.claimQuantity(claimQuantity);
            
            // RULE 3 - WITHHOLDING TAX ON REAL DIVIDEND
            BigDecimal withholdingTaxRateApplied = BigDecimal.ZERO;
            BigDecimal grossRealDividend = BigDecimal.ZERO;
            BigDecimal withholdingTaxAmount = BigDecimal.ZERO;
            BigDecimal netRealDividend = BigDecimal.ZERO;
            
            if (eligibleQuantity > 0 && taxRuleOpt.isPresent()) {
                WithholdingTaxRule taxRule = taxRuleOpt.get();
                grossRealDividend = BigDecimal.valueOf(eligibleQuantity)
                        .multiply(declaration.getDeclaredDividendPerShare());
                
                if (client.isTaxExempt() && taxRule.getTreatyRatePercent().compareTo(BigDecimal.ZERO) == 0) {
                    withholdingTaxRateApplied = BigDecimal.ZERO;
                    notes.add("Client is tax-exempt and treaty provides full exemption. WHT rate: 0%");
                } else if (taxRule.getTreatyRatePercent().compareTo(taxRule.getStatutoryRatePercent()) < 0) {
                    withholdingTaxRateApplied = taxRule.getTreatyRatePercent();
                    notes.add("Treaty relief applied: statutory " + taxRule.getStatutoryRatePercent() + 
                             "% reduced to treaty " + taxRule.getTreatyRatePercent() + 
                             "% for " + client.getClientType() + " domiciled in " + client.getDomicile());
                } else {
                    withholdingTaxRateApplied = taxRule.getStatutoryRatePercent();
                    notes.add("Statutory withholding tax rate of " + taxRule.getStatutoryRatePercent() + 
                             "% applied. No treaty relief available.");
                }
                
                withholdingTaxAmount = grossRealDividend
                        .multiply(withholdingTaxRateApplied)
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                netRealDividend = grossRealDividend.subtract(withholdingTaxAmount);
            }
            
            resultBuilder.grossRealDividend(grossRealDividend)
                    .withholdingTaxRateApplied(withholdingTaxRateApplied)
                    .withholdingTaxAmount(withholdingTaxAmount)
                    .netRealDividend(netRealDividend);
            
            // RULE 4 - MANUFACTURED DIVIDEND PROCESSING
            BigDecimal grossManufacturedDividend = BigDecimal.ZERO;
            BigDecimal manufacturedWithholdingTaxRate = BigDecimal.ZERO;
            BigDecimal manufacturedWithholdingTaxAmount = BigDecimal.ZERO;
            BigDecimal netManufacturedDividend = BigDecimal.ZERO;
            BigDecimal grossUpAmount = BigDecimal.ZERO;
            BigDecimal netManufacturedDividendAfterGrossUp = BigDecimal.ZERO;
            String manufacturedDividendBorrower = null;
            
            if (position.getOnLoanQuantity() > 0 && loanOpt.isPresent() && taxRuleOpt.isPresent()) {
                StockLoanPosition loan = loanOpt.get();
                WithholdingTaxRule taxRule = taxRuleOpt.get();
                
                manufacturedDividendBorrower = loan.getBorrowerName();
                
                grossManufacturedDividend = BigDecimal.valueOf(position.getOnLoanQuantity())
                        .multiply(declaration.getDeclaredDividendPerShare());
                
                notes.add("Securities lending position of " + position.getOnLoanQuantity() + 
                         " shares on loan to " + loan.getBorrowerName() + 
                         ". Dividend will be received as manufactured payment, not real dividend.");
                
                // Always apply statutory rate for manufactured dividend
                manufacturedWithholdingTaxRate = taxRule.getStatutoryRatePercent();
                notes.add("Treaty relief does NOT apply to manufactured dividend. Statutory WHT rate of " + 
                         manufacturedWithholdingTaxRate + "% applied regardless of client treaty status.");
                
                manufacturedWithholdingTaxAmount = grossManufacturedDividend
                        .multiply(manufacturedWithholdingTaxRate)
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                netManufacturedDividend = grossManufacturedDividend.subtract(manufacturedWithholdingTaxAmount);
                
                if (loan.isGrossUpAgreed()) {
                    // Calculate gross-up amount (tax that would have been saved under treaty)
                    BigDecimal treatyTaxAmount = grossManufacturedDividend
                            .multiply(taxRule.getTreatyRatePercent())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    grossUpAmount = manufacturedWithholdingTaxAmount.subtract(treatyTaxAmount);
                    
                    notes.add("Gross-up clause active under " + loan.getAgreementType() + 
                             ". Borrower (" + loan.getBorrowerName() + 
                             ") obligated to pay additional " + grossUpAmount + 
                             " to compensate for lost treaty benefit.");
                } else {
                    BigDecimal taxLeakage = manufacturedWithholdingTaxAmount.subtract(
                            grossManufacturedDividend
                                    .multiply(taxRule.getTreatyRatePercent())
                                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                    
                    notes.add("No gross-up clause in " + loan.getAgreementType() + 
                             " agreement with " + loan.getBorrowerName() + 
                             ". Tax leakage of " + taxLeakage + " on manufactured dividend borne by lender.");
                }
                
                netManufacturedDividendAfterGrossUp = netManufacturedDividend.add(grossUpAmount);
            }
            
            resultBuilder.grossManufacturedDividend(grossManufacturedDividend)
                    .manufacturedWithholdingTaxRate(manufacturedWithholdingTaxRate)
                    .manufacturedWithholdingTaxAmount(manufacturedWithholdingTaxAmount)
                    .netManufacturedDividend(netManufacturedDividend)
                    .grossUpAmount(grossUpAmount)
                    .netManufacturedDividendAfterGrossUp(netManufacturedDividendAfterGrossUp)
                    .manufacturedDividendBorrower(manufacturedDividendBorrower);
            
            // RULE 5 - DIVIDEND CLAIM AMOUNT
            BigDecimal dividendClaimAmount = BigDecimal.ZERO;
            if (claimQuantity > 0) {
                dividendClaimAmount = BigDecimal.valueOf(claimQuantity)
                        .multiply(declaration.getDeclaredDividendPerShare());
                notes.add("Gross dividend claim of " + dividendClaimAmount + " " + declaration.getCurrency() + 
                         " to be raised for " + claimQuantity + " shares trading cum-dividend.");
            }
            resultBuilder.dividendClaimAmount(dividendClaimAmount);
            
            // RULE 6 - TOTAL NET ENTITLEMENT
            BigDecimal totalNetEntitlement = netRealDividend
                    .add(netManufacturedDividendAfterGrossUp)
                    .add(dividendClaimAmount);
            resultBuilder.totalNetEntitlement(totalNetEntitlement);
            
            // RULE 7 - ELIGIBILITY STATUS
            EligibilityStatus status;
            if (eligibleQuantity == 0 && position.getOnLoanQuantity() > 0 && claimQuantity == 0) {
                status = EligibilityStatus.MANUFACTURED_ONLY;
            } else if (eligibleQuantity == 0 && position.getOnLoanQuantity() == 0) {
                status = EligibilityStatus.INELIGIBLE;
            } else if (claimQuantity > 0 && eligibleQuantity > 0) {
                status = EligibilityStatus.CLAIM_REQUIRED;
            } else if (position.getOnLoanQuantity() > 0 && eligibleQuantity > 0) {
                status = EligibilityStatus.PARTIALLY_ELIGIBLE;
            } else {
                status = EligibilityStatus.FULLY_ELIGIBLE;
            }
            resultBuilder.eligibilityStatus(status);
            
            results.add(resultBuilder.build());
        }
        
        return results;
    }

    public Map<String, List<EligibilityResult>> processAllDeclarations() {
        return mockDataService.getDividendDeclarations().stream()
                .collect(Collectors.toMap(
                    DividendDeclaration::getDeclarationId,
                    declaration -> processDeclaration(declaration.getDeclarationId())
                ));
    }
}
