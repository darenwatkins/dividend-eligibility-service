package com.custody.eligibility.service;

import com.custody.eligibility.model.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class MockDataService {

    private static final String DEUTSCHE_BANK_ISIN = "DE0005140008";
    private static final LocalDate RECORD_DATE = LocalDate.of(2025, 7, 15);
    private static final LocalDate PAY_DATE = LocalDate.of(2025, 7, 22);

    public List<Client> getClients() {
        return List.of(
            Client.builder()
                .clientId("C001")
                .clientName("Oslo Pension Fund")
                .domicile("NO")
                .clientType(ClientType.PENSION_FUND)
                .taxExempt(true)
                .build(),
            
            Client.builder()
                .clientId("C002")
                .clientName("Blackrock Asset Management")
                .domicile("US")
                .clientType(ClientType.ASSET_MANAGER)
                .taxExempt(false)
                .build(),
            
            Client.builder()
                .clientId("C003")
                .clientName("Abu Dhabi SWF")
                .domicile("AE")
                .clientType(ClientType.SOVEREIGN_WEALTH)
                .taxExempt(true)
                .build(),
            
            Client.builder()
                .clientId("C004")
                .clientName("Allianz Insurance")
                .domicile("DE")
                .clientType(ClientType.INSURANCE)
                .taxExempt(false)
                .build(),
            
            Client.builder()
                .clientId("C005")
                .clientName("Retail Investor GmbH")
                .domicile("DE")
                .clientType(ClientType.RETAIL)
                .taxExempt(false)
                .build()
        );
    }

    private String getClientNameById(String clientId) {
        return getClients().stream()
                .filter(client -> client.getClientId().equals(clientId))
                .findFirst()
                .map(Client::getClientName)
                .orElse("Unknown Client");
    }

    public List<SecurityPosition> getSecurityPositions() {
        return List.of(
            SecurityPosition.builder()
                .positionId("P001")
                .clientId("C001")
                .isin(DEUTSCHE_BANK_ISIN)
                .settledQuantity(100000)
                .pendingSettlementQuantity(5000)
                .onLoanQuantity(20000)
                .recordDate(RECORD_DATE)
                .build(),
            
            SecurityPosition.builder()
                .positionId("P002")
                .clientId("C002")
                .isin(DEUTSCHE_BANK_ISIN)
                .settledQuantity(250000)
                .pendingSettlementQuantity(-10000)
                .onLoanQuantity(0)
                .recordDate(RECORD_DATE)
                .build(),
            
            SecurityPosition.builder()
                .positionId("P003")
                .clientId("C003")
                .isin(DEUTSCHE_BANK_ISIN)
                .settledQuantity(75000)
                .pendingSettlementQuantity(0)
                .onLoanQuantity(75000)
                .recordDate(RECORD_DATE)
                .build(),
            
            SecurityPosition.builder()
                .positionId("P004")
                .clientId("C004")
                .isin(DEUTSCHE_BANK_ISIN)
                .settledQuantity(50000)
                .pendingSettlementQuantity(8000)
                .onLoanQuantity(10000)
                .recordDate(RECORD_DATE)
                .build(),
            
            SecurityPosition.builder()
                .positionId("P005")
                .clientId("C005")
                .isin(DEUTSCHE_BANK_ISIN)
                .settledQuantity(1000)
                .pendingSettlementQuantity(0)
                .onLoanQuantity(0)
                .recordDate(RECORD_DATE)
                .build()
        );
    }

    public List<StockLoanPosition> getStockLoanPositions() {
        return List.of(
            StockLoanPosition.builder()
                .loanId("L001")
                .lenderClientId("C001")
                .borrowerName("Goldman Sachs")
                .isin(DEUTSCHE_BANK_ISIN)
                .loanedQuantity(20000)
                .loanOpenDate(LocalDate.of(2025, 6, 1))
                .expectedReturnDate(LocalDate.of(2025, 8, 1))
                .grossUpAgreed(true)
                .agreementType(AgreementType.GMSLA)
                .build(),
            
            StockLoanPosition.builder()
                .loanId("L002")
                .lenderClientId("C003")
                .borrowerName("Morgan Stanley")
                .isin(DEUTSCHE_BANK_ISIN)
                .loanedQuantity(75000)
                .loanOpenDate(LocalDate.of(2025, 6, 15))
                .expectedReturnDate(LocalDate.of(2025, 8, 15))
                .grossUpAgreed(false)
                .agreementType(AgreementType.GMSLA)
                .build(),
            
            StockLoanPosition.builder()
                .loanId("L003")
                .lenderClientId("C004")
                .borrowerName("JP Morgan")
                .isin(DEUTSCHE_BANK_ISIN)
                .loanedQuantity(10000)
                .loanOpenDate(LocalDate.of(2025, 6, 10))
                .expectedReturnDate(LocalDate.of(2025, 8, 10))
                .grossUpAgreed(true)
                .agreementType(AgreementType.TOTAL_RETURN_SWAP)
                .build()
        );
    }

    public List<DividendDeclaration> getDividendDeclarations() {
        return List.of(
            DividendDeclaration.builder()
                .declarationId("DECL001")
                .isin(DEUTSCHE_BANK_ISIN)
                .issuerName("Deutsche Bank AG")
                .declaredDividendPerShare(new BigDecimal("0.45"))
                .currency("EUR")
                .dividendType(DividendType.ORDINARY)
                .declarationDate(LocalDate.of(2025, 6, 20))
                .exDate(LocalDate.of(2025, 7, 14))
                .recordDate(RECORD_DATE)
                .payDate(PAY_DATE)
                .build()
        );
    }

    public List<WithholdingTaxRule> getWithholdingTaxRules() {
        return List.of(
            // PENSION_FUND + NO domicile: full exemption
            WithholdingTaxRule.builder()
                .ruleId("RULE001")
                .isin(DEUTSCHE_BANK_ISIN)
                .marketCountryCode("DE")
                .clientType(ClientType.PENSION_FUND)
                .clientDomicile("NO")
                .statutoryRatePercent(new BigDecimal("25"))
                .treatyRatePercent(new BigDecimal("0"))
                .treatyApplicableToManufactured(false)
                .build(),
            
            // SOVEREIGN_WEALTH + AE domicile: full exemption
            WithholdingTaxRule.builder()
                .ruleId("RULE002")
                .isin(DEUTSCHE_BANK_ISIN)
                .marketCountryCode("DE")
                .clientType(ClientType.SOVEREIGN_WEALTH)
                .clientDomicile("AE")
                .statutoryRatePercent(new BigDecimal("25"))
                .treatyRatePercent(new BigDecimal("0"))
                .treatyApplicableToManufactured(false)
                .build(),
            
            // ASSET_MANAGER + US domicile: treaty reduction
            WithholdingTaxRule.builder()
                .ruleId("RULE003")
                .isin(DEUTSCHE_BANK_ISIN)
                .marketCountryCode("DE")
                .clientType(ClientType.ASSET_MANAGER)
                .clientDomicile("US")
                .statutoryRatePercent(new BigDecimal("25"))
                .treatyRatePercent(new BigDecimal("15"))
                .treatyApplicableToManufactured(false)
                .build(),
            
            // INSURANCE + DE domicile: domestic, no reduction
            WithholdingTaxRule.builder()
                .ruleId("RULE004")
                .isin(DEUTSCHE_BANK_ISIN)
                .marketCountryCode("DE")
                .clientType(ClientType.INSURANCE)
                .clientDomicile("DE")
                .statutoryRatePercent(new BigDecimal("25"))
                .treatyRatePercent(new BigDecimal("25"))
                .treatyApplicableToManufactured(false)
                .build(),
            
            // RETAIL + DE domicile: domestic, no reduction
            WithholdingTaxRule.builder()
                .ruleId("RULE005")
                .isin(DEUTSCHE_BANK_ISIN)
                .marketCountryCode("DE")
                .clientType(ClientType.RETAIL)
                .clientDomicile("DE")
                .statutoryRatePercent(new BigDecimal("25"))
                .treatyRatePercent(new BigDecimal("25"))
                .treatyApplicableToManufactured(false)
                .build()
        );
    }
}
