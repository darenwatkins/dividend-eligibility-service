package com.custody.eligibility.util;

import java.math.BigDecimal;

public class TemplateUtils {
    
    public static String formatCurrency(BigDecimal amount, String currencyCode) {
        return CurrencyFormatter.formatCurrencyWithSymbol(amount, currencyCode);
    }
    
    public static String formatCurrency(BigDecimal amount) {
        // Default to EUR if no currency specified
        return formatCurrency(amount, "EUR");
    }
}
