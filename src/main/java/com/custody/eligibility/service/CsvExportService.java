package com.custody.eligibility.service;

import com.custody.eligibility.model.EligibilityResult;
import com.custody.eligibility.util.CurrencyFormatter;
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
        csv.append("Currency:,").append(result.getCurrency() != null ? result.getCurrency() : "N/A").append("\n");
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
        csv.append("Gross Real Dividend,").append(formatCurrencyWithCode(result.getGrossRealDividend(), result.getCurrency())).append("\n");
        csv.append("Withholding Tax Rate Applied,").append(formatPercentage(result.getWithholdingTaxRateApplied())).append("\n");
        csv.append("Withholding Tax Amount,").append(formatCurrencyWithCode(result.getWithholdingTaxAmount(), result.getCurrency())).append("\n");
        csv.append("Net Real Dividend,").append(formatCurrencyWithCode(result.getNetRealDividend(), result.getCurrency())).append("\n\n");
        
        // Manufactured Dividend Entitlement (if applicable)
        if (result.getOnLoanQuantity() > 0) {
            csv.append("Manufactured Dividend Entitlement\n");
            csv.append("Gross Manufactured Dividend,").append(formatCurrencyWithCode(result.getGrossManufacturedDividend(), result.getCurrency())).append("\n");
            csv.append("Statutory WHT Rate,").append(formatPercentage(result.getManufacturedWithholdingTaxRate())).append("\n");
            csv.append("Manufactured WHT Amount,").append(formatCurrencyWithCode(result.getManufacturedWithholdingTaxAmount(), result.getCurrency())).append("\n");
            csv.append("Net Manufactured Dividend (pre gross-up),").append(formatCurrencyWithCode(result.getNetManufacturedDividend(), result.getCurrency())).append("\n");
            csv.append("Gross-Up Amount,").append(formatCurrencyWithCode(result.getGrossUpAmount(), result.getCurrency())).append("\n");
            csv.append("Net Manufactured Dividend (post gross-up),").append(formatCurrencyWithCode(result.getNetManufacturedDividendAfterGrossUp(), result.getCurrency())).append("\n\n");
        }
        
        // Dividend Claim (if applicable)
        if (result.getClaimQuantity() > 0) {
            csv.append("Dividend Claim\n");
            csv.append("Claim Quantity,").append(QUANTITY_FORMAT.format(result.getClaimQuantity())).append("\n");
            csv.append("Gross Claim Amount,").append(formatCurrencyWithCode(result.getDividendClaimAmount(), result.getCurrency())).append("\n\n");
        }
        
        // Total Net Entitlement
        csv.append("Total Net Entitlement\n");
        csv.append("Total Net Entitlement,").append(formatCurrencyWithCode(result.getTotalNetEntitlement(), result.getCurrency())).append("\n\n");
        
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
    
    private String formatCurrencyWithCode(BigDecimal amount, String currencyCode) {
        if (amount == null) return "0.00";
        if (currencyCode == null) return CURRENCY_FORMAT.format(amount);
        return CurrencyFormatter.formatCurrency(amount, currencyCode);
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
