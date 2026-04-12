package com.custody.eligibility.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    
    public static String formatCurrency(BigDecimal amount, String currencyCode) {
        if (amount == null) {
            return "";
        }
        
        NumberFormat currencyFormat;
        Locale locale;
        
        // Determine the appropriate locale based on currency code
        switch (currencyCode.toUpperCase()) {
            case "EUR":
                locale = Locale.GERMANY; // Use German locale for Euro formatting
                break;
            case "USD":
                locale = Locale.US; // Use US locale for USD formatting
                break;
            case "GBP":
                locale = Locale.UK; // Use UK locale for GBP formatting
                break;
            case "JPY":
                locale = Locale.JAPAN; // Use Japanese locale for Yen formatting
                break;
            default:
                // For unknown currencies, use a generic format with currency code
                return String.format("%s %.2f", currencyCode, amount);
        }
        
        currencyFormat = NumberFormat.getCurrencyInstance(locale);
        return currencyFormat.format(amount);
    }
    
    public static String formatCurrencyWithSymbol(BigDecimal amount, String currencyCode) {
        if (amount == null) {
            return "";
        }
        
        // For more control over the symbol, we can format manually
        switch (currencyCode.toUpperCase()) {
            case "EUR":
                return String.format("€%.2f", amount);
            case "USD":
                return String.format("$%.2f", amount);
            case "GBP":
                return String.format("£%.2f", amount);
            case "JPY":
                return String.format("¥%.0f", amount); // Yen typically doesn't show decimals
            default:
                return String.format("%s %.2f", currencyCode, amount);
        }
    }
}
