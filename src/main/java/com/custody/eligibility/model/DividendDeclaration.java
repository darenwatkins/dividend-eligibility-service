package com.custody.eligibility.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DividendDeclaration {
    private String declarationId;
    private String isin;
    private String issuerName;
    private BigDecimal declaredDividendPerShare;
    private String currency;
    private LocalDate declarationDate;
    private LocalDate exDate;
    private LocalDate recordDate;
    private LocalDate payDate;
    private DividendType dividendType;
}
