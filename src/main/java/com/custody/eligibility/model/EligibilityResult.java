package com.custody.eligibility.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligibilityResult {
    private String resultId;
    private String clientId;
    private String clientName;
    private String declarationId;
    private String isin;
    private String issuerName;

    // Position breakdown
    private long settledQuantity;
    private long onLoanQuantity;
    private long pendingSettlementQuantity;
    private long eligibleQuantity;
    private long claimQuantity;

    // Real dividend entitlement (on eligible settled quantity)
    private BigDecimal grossRealDividend;
    private BigDecimal withholdingTaxRateApplied;
    private BigDecimal withholdingTaxAmount;
    private BigDecimal netRealDividend;

    // Manufactured dividend entitlement (on on-loan quantity)
    private BigDecimal grossManufacturedDividend;
    private BigDecimal manufacturedWithholdingTaxRate;
    private BigDecimal manufacturedWithholdingTaxAmount;
    private BigDecimal netManufacturedDividend;
    private BigDecimal grossUpAmount;
    private BigDecimal netManufacturedDividendAfterGrossUp;

    // Claims
    private BigDecimal dividendClaimAmount;

    // Totals
    private BigDecimal totalNetEntitlement;
    private EligibilityStatus eligibilityStatus;
    private List<String> processingNotes;
}
