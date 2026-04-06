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
public class StockLoanPosition {
    private String loanId;
    private String lenderClientId;
    private String borrowerName;
    private String isin;
    private long loanedQuantity;
    private LocalDate loanOpenDate;
    private LocalDate expectedReturnDate;
    private boolean grossUpAgreed;
    private AgreementType agreementType;
}
