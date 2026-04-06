package com.custody.eligibility.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithholdingTaxRule {
    private String ruleId;
    private String isin;
    private String marketCountryCode;
    private ClientType clientType;
    private String clientDomicile;
    private BigDecimal statutoryRatePercent;
    private BigDecimal treatyRatePercent;
    private boolean treatyApplicableToManufactured;
}
