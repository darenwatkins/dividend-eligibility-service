package com.custody.eligibility.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityPosition {
    private String positionId;
    private String clientId;
    private String isin;
    private long settledQuantity;
    private long pendingSettlementQuantity;
    private long onLoanQuantity;
    private LocalDate recordDate;
}
