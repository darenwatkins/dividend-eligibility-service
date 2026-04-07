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
            // Deutsche Bank positions (original)
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
                .build(),
            
            // Volkswagen positions
            SecurityPosition.builder()
                .positionId("P006")
                .clientId("C001")
                .isin("DE0007664039")
                .settledQuantity(50000)
                .pendingSettlementQuantity(2000)
                .onLoanQuantity(15000)
                .recordDate(LocalDate.of(2025, 7, 21))
                .build(),
            
            SecurityPosition.builder()
                .positionId("P007")
                .clientId("C002")
                .isin("DE0007664039")
                .settledQuantity(120000)
                .pendingSettlementQuantity(0)
                .onLoanQuantity(25000)
                .recordDate(LocalDate.of(2025, 7, 21))
                .build(),
            
            // Siemens positions
            SecurityPosition.builder()
                .positionId("P008")
                .clientId("C003")
                .isin("DE0007236101")
                .settledQuantity(80000)
                .pendingSettlementQuantity(3000)
                .onLoanQuantity(20000)
                .recordDate(LocalDate.of(2025, 7, 13))
                .build(),
            
            SecurityPosition.builder()
                .positionId("P009")
                .clientId("C004")
                .isin("DE0007236101")
                .settledQuantity(60000)
                .pendingSettlementQuantity(5000)
                .onLoanQuantity(10000)
                .recordDate(LocalDate.of(2025, 7, 13))
                .build(),
            
            // Allianz positions
            SecurityPosition.builder()
                .positionId("P010")
                .clientId("C001")
                .isin("DE0008404005")
                .settledQuantity(30000)
                .pendingSettlementQuantity(1000)
                .onLoanQuantity(5000)
                .recordDate(LocalDate.of(2025, 7, 19))
                .build(),
            
            SecurityPosition.builder()
                .positionId("P011")
                .clientId("C005")
                .isin("DE0008404005")
                .settledQuantity(15000)
                .pendingSettlementQuantity(0)
                .onLoanQuantity(0)
                .recordDate(LocalDate.of(2025, 7, 19))
                .build(),
            
            // Bayer positions (USD)
            SecurityPosition.builder()
                .positionId("P012")
                .clientId("C002")
                .isin("DE000BAY0017")
                .settledQuantity(90000)
                .pendingSettlementQuantity(4000)
                .onLoanQuantity(18000)
                .recordDate(LocalDate.of(2025, 7, 17))
                .build(),
            
            SecurityPosition.builder()
                .positionId("P013")
                .clientId("C003")
                .isin("DE000BAY0017")
                .settledQuantity(45000)
                .pendingSettlementQuantity(0)
                .onLoanQuantity(12000)
                .recordDate(LocalDate.of(2025, 7, 17))
                .build(),
            
            // SAP positions (Stock dividend)
            SecurityPosition.builder()
                .positionId("P014")
                .clientId("C004")
                .isin("DE0007164600")
                .settledQuantity(70000)
                .pendingSettlementQuantity(2000)
                .onLoanQuantity(14000)
                .recordDate(LocalDate.of(2025, 7, 25))
                .build(),
            
            SecurityPosition.builder()
                .positionId("P015")
                .clientId("C005")
                .isin("DE0007164600")
                .settledQuantity(25000)
                .pendingSettlementQuantity(0)
                .onLoanQuantity(0)
                .recordDate(LocalDate.of(2025, 7, 25))
                .build(),
            
            // BMW positions
            SecurityPosition.builder()
                .positionId("P016")
                .clientId("C001")
                .isin("DE0005190003")
                .settledQuantity(40000)
                .pendingSettlementQuantity(1500)
                .onLoanQuantity(8000)
                .recordDate(LocalDate.of(2025, 7, 10))
                .build(),
            
            SecurityPosition.builder()
                .positionId("P017")
                .clientId("C002")
                .isin("DE0005190003")
                .settledQuantity(85000)
                .pendingSettlementQuantity(0)
                .onLoanQuantity(20000)
                .recordDate(LocalDate.of(2025, 7, 10))
                .build()
        );
    }

    public List<StockLoanPosition> getStockLoanPositions() {
        return List.of(
            // Deutsche Bank loans (original)
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
                .build(),
            
            // Volkswagen loans
            StockLoanPosition.builder()
                .loanId("L004")
                .lenderClientId("C001")
                .borrowerName("UBS")
                .isin("DE0007664039")
                .loanedQuantity(15000)
                .loanOpenDate(LocalDate.of(2025, 6, 20))
                .expectedReturnDate(LocalDate.of(2025, 8, 20))
                .grossUpAgreed(true)
                .agreementType(AgreementType.GMSLA)
                .build(),
            
            StockLoanPosition.builder()
                .loanId("L005")
                .lenderClientId("C002")
                .borrowerName("Credit Suisse")
                .isin("DE0007664039")
                .loanedQuantity(25000)
                .loanOpenDate(LocalDate.of(2025, 6, 25))
                .expectedReturnDate(LocalDate.of(2025, 8, 25))
                .grossUpAgreed(false)
                .agreementType(AgreementType.TOTAL_RETURN_SWAP)
                .build(),
            
            // Siemens loans
            StockLoanPosition.builder()
                .loanId("L006")
                .lenderClientId("C003")
                .borrowerName("Deutsche Bank")
                .isin("DE0007236101")
                .loanedQuantity(20000)
                .loanOpenDate(LocalDate.of(2025, 6, 18))
                .expectedReturnDate(LocalDate.of(2025, 8, 18))
                .grossUpAgreed(true)
                .agreementType(AgreementType.GMSLA)
                .build(),
            
            StockLoanPosition.builder()
                .loanId("L007")
                .lenderClientId("C004")
                .borrowerName("Barclays")
                .isin("DE0007236101")
                .loanedQuantity(10000)
                .loanOpenDate(LocalDate.of(2025, 6, 22))
                .expectedReturnDate(LocalDate.of(2025, 8, 22))
                .grossUpAgreed(false)
                .agreementType(AgreementType.GMSLA)
                .build(),
            
            // Allianz loans
            StockLoanPosition.builder()
                .loanId("L008")
                .lenderClientId("C001")
                .borrowerName("BNP Paribas")
                .isin("DE0008404005")
                .loanedQuantity(5000)
                .loanOpenDate(LocalDate.of(2025, 6, 28))
                .expectedReturnDate(LocalDate.of(2025, 8, 28))
                .grossUpAgreed(true)
                .agreementType(AgreementType.GMSLA)
                .build(),
            
            // Bayer loans (USD)
            StockLoanPosition.builder()
                .loanId("L009")
                .lenderClientId("C002")
                .borrowerName("Citigroup")
                .isin("DE000BAY0017")
                .loanedQuantity(18000)
                .loanOpenDate(LocalDate.of(2025, 6, 30))
                .expectedReturnDate(LocalDate.of(2025, 8, 30))
                .grossUpAgreed(false)
                .agreementType(AgreementType.TOTAL_RETURN_SWAP)
                .build(),
            
            StockLoanPosition.builder()
                .loanId("L010")
                .lenderClientId("C003")
                .borrowerName("Bank of America")
                .isin("DE000BAY0017")
                .loanedQuantity(12000)
                .loanOpenDate(LocalDate.of(2025, 7, 1))
                .expectedReturnDate(LocalDate.of(2025, 9, 1))
                .grossUpAgreed(true)
                .agreementType(AgreementType.GMSLA)
                .build(),
            
            // SAP loans (Stock dividend)
            StockLoanPosition.builder()
                .loanId("L011")
                .lenderClientId("C004")
                .borrowerName("Goldman Sachs")
                .isin("DE0007164600")
                .loanedQuantity(14000)
                .loanOpenDate(LocalDate.of(2025, 7, 5))
                .expectedReturnDate(LocalDate.of(2025, 9, 5))
                .grossUpAgreed(false)
                .agreementType(AgreementType.TOTAL_RETURN_SWAP)
                .build(),
            
            // BMW loans
            StockLoanPosition.builder()
                .loanId("L012")
                .lenderClientId("C001")
                .borrowerName("Morgan Stanley")
                .isin("DE0005190003")
                .loanedQuantity(8000)
                .loanOpenDate(LocalDate.of(2025, 7, 2))
                .expectedReturnDate(LocalDate.of(2025, 9, 2))
                .grossUpAgreed(true)
                .agreementType(AgreementType.GMSLA)
                .build(),
            
            StockLoanPosition.builder()
                .loanId("L013")
                .lenderClientId("C002")
                .borrowerName("UBS")
                .isin("DE0005190003")
                .loanedQuantity(20000)
                .loanOpenDate(LocalDate.of(2025, 7, 8))
                .expectedReturnDate(LocalDate.of(2025, 9, 8))
                .grossUpAgreed(false)
                .agreementType(AgreementType.TOTAL_RETURN_SWAP)
                .build()
        );
    }

    public List<DividendDeclaration> getDividendDeclarations() {
        return List.of(
            // Deutsche Bank - Ordinary Dividend
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
                .build(),
            
            // Volkswagen - Special Dividend
            DividendDeclaration.builder()
                .declarationId("DECL002")
                .isin("DE0007664039")
                .issuerName("Volkswagen AG")
                .declaredDividendPerShare(new BigDecimal("2.10"))
                .currency("EUR")
                .dividendType(DividendType.SPECIAL)
                .declarationDate(LocalDate.of(2025, 6, 25))
                .exDate(LocalDate.of(2025, 7, 18))
                .recordDate(LocalDate.of(2025, 7, 21))
                .payDate(LocalDate.of(2025, 7, 28))
                .build(),
            
            // Siemens - Interim Dividend
            DividendDeclaration.builder()
                .declarationId("DECL003")
                .isin("DE0007236101")
                .issuerName("Siemens AG")
                .declaredDividendPerShare(new BigDecimal("0.85"))
                .currency("EUR")
                .dividendType(DividendType.INTERIM)
                .declarationDate(LocalDate.of(2025, 6, 15))
                .exDate(LocalDate.of(2025, 7, 10))
                .recordDate(LocalDate.of(2025, 7, 13))
                .payDate(LocalDate.of(2025, 7, 20))
                .build(),
            
            // Allianz - Final Dividend
            DividendDeclaration.builder()
                .declarationId("DECL004")
                .isin("DE0008404005")
                .issuerName("Allianz SE")
                .declaredDividendPerShare(new BigDecimal("5.20"))
                .currency("EUR")
                .dividendType(DividendType.FINAL)
                .declarationDate(LocalDate.of(2025, 6, 18))
                .exDate(LocalDate.of(2025, 7, 16))
                .recordDate(LocalDate.of(2025, 7, 19))
                .payDate(LocalDate.of(2025, 7, 26))
                .build(),
            
            // Bayer - Ordinary Dividend (USD)
            DividendDeclaration.builder()
                .declarationId("DECL005")
                .isin("DE000BAY0017")
                .issuerName("Bayer AG")
                .declaredDividendPerShare(new BigDecimal("0.40"))
                .currency("USD")
                .dividendType(DividendType.ORDINARY)
                .declarationDate(LocalDate.of(2025, 6, 22))
                .exDate(LocalDate.of(2025, 7, 15))
                .recordDate(LocalDate.of(2025, 7, 17))
                .payDate(LocalDate.of(2025, 7, 24))
                .build(),
            
            // SAP - Stock Dividend
            DividendDeclaration.builder()
                .declarationId("DECL006")
                .isin("DE0007164600")
                .issuerName("SAP SE")
                .declaredDividendPerShare(new BigDecimal("0.00"))
                .currency("EUR")
                .dividendType(DividendType.STOCK)
                .declarationDate(LocalDate.of(2025, 6, 28))
                .exDate(LocalDate.of(2025, 7, 22))
                .recordDate(LocalDate.of(2025, 7, 25))
                .payDate(LocalDate.of(2025, 8, 1))
                .build(),
            
            // BMW - Ordinary Dividend (Higher amount)
            DividendDeclaration.builder()
                .declarationId("DECL007")
                .isin("DE0005190003")
                .issuerName("BMW AG")
                .declaredDividendPerShare(new BigDecimal("4.00"))
                .currency("EUR")
                .dividendType(DividendType.ORDINARY)
                .declarationDate(LocalDate.of(2025, 6, 12))
                .exDate(LocalDate.of(2025, 7, 8))
                .recordDate(LocalDate.of(2025, 7, 10))
                .payDate(LocalDate.of(2025, 7, 17))
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
                .build(),
            
            // Volkswagen Rules
            // PENSION_FUND + NO domicile: full exemption
            WithholdingTaxRule.builder()
                .ruleId("RULE006")
                .isin("DE0007664039")
                .marketCountryCode("DE")
                .clientType(ClientType.PENSION_FUND)
                .clientDomicile("NO")
                .statutoryRatePercent(new BigDecimal("25"))
                .treatyRatePercent(new BigDecimal("0"))
                .treatyApplicableToManufactured(false)
                .build(),
            
            // ASSET_MANAGER + US domicile: 15% treaty rate
            WithholdingTaxRule.builder()
                .ruleId("RULE007")
                .isin("DE0007664039")
                .marketCountryCode("DE")
                .clientType(ClientType.ASSET_MANAGER)
                .clientDomicile("US")
                .statutoryRatePercent(new BigDecimal("25"))
                .treatyRatePercent(new BigDecimal("15"))
                .treatyApplicableToManufactured(true)
                .build(),
            
            // Siemens Rules
            // SOVEREIGN_WEALTH + AE domicile: full exemption
            WithholdingTaxRule.builder()
                .ruleId("RULE008")
                .isin("DE0007236101")
                .marketCountryCode("DE")
                .clientType(ClientType.SOVEREIGN_WEALTH)
                .clientDomicile("AE")
                .statutoryRatePercent(new BigDecimal("25"))
                .treatyRatePercent(new BigDecimal("0"))
                .treatyApplicableToManufactured(false)
                .build(),
            
            // ASSET_MANAGER + US domicile: 15% treaty rate
            WithholdingTaxRule.builder()
                .ruleId("RULE009")
                .isin("DE0007236101")
                .marketCountryCode("DE")
                .clientType(ClientType.ASSET_MANAGER)
                .clientDomicile("US")
                .statutoryRatePercent(new BigDecimal("25"))
                .treatyRatePercent(new BigDecimal("15"))
                .treatyApplicableToManufactured(true)
                .build(),
            
            // Allianz Rules
            // PENSION_FUND + NO domicile: full exemption
            WithholdingTaxRule.builder()
                .ruleId("RULE010")
                .isin("DE0008404005")
                .marketCountryCode("DE")
                .clientType(ClientType.PENSION_FUND)
                .clientDomicile("NO")
                .statutoryRatePercent(new BigDecimal("25"))
                .treatyRatePercent(new BigDecimal("0"))
                .treatyApplicableToManufactured(false)
                .build(),
            
            // RETAIL + DE domicile: full statutory rate
            WithholdingTaxRule.builder()
                .ruleId("RULE011")
                .isin("DE0008404005")
                .marketCountryCode("DE")
                .clientType(ClientType.RETAIL)
                .clientDomicile("DE")
                .statutoryRatePercent(new BigDecimal("25"))
                .treatyRatePercent(new BigDecimal("25"))
                .treatyApplicableToManufactured(false)
                .build(),
            
            // Bayer Rules (USD)
            // ASSET_MANAGER + US domicile: 30% statutory, 15% treaty
            WithholdingTaxRule.builder()
                .ruleId("RULE012")
                .isin("DE000BAY0017")
                .marketCountryCode("DE")
                .clientType(ClientType.ASSET_MANAGER)
                .clientDomicile("US")
                .statutoryRatePercent(new BigDecimal("30"))
                .treatyRatePercent(new BigDecimal("15"))
                .treatyApplicableToManufactured(true)
                .build(),
            
            // SOVEREIGN_WEALTH + AE domicile: 30% statutory, 0% treaty
            WithholdingTaxRule.builder()
                .ruleId("RULE013")
                .isin("DE000BAY0017")
                .marketCountryCode("DE")
                .clientType(ClientType.SOVEREIGN_WEALTH)
                .clientDomicile("AE")
                .statutoryRatePercent(new BigDecimal("30"))
                .treatyRatePercent(new BigDecimal("0"))
                .treatyApplicableToManufactured(false)
                .build(),
            
            // SAP Rules (Stock dividend - no withholding)
            // ASSET_MANAGER + US domicile: 0% for stock dividends
            WithholdingTaxRule.builder()
                .ruleId("RULE014")
                .isin("DE0007164600")
                .marketCountryCode("DE")
                .clientType(ClientType.ASSET_MANAGER)
                .clientDomicile("US")
                .statutoryRatePercent(new BigDecimal("0"))
                .treatyRatePercent(new BigDecimal("0"))
                .treatyApplicableToManufactured(false)
                .build(),
            
            // RETAIL + DE domicile: 0% for stock dividends
            WithholdingTaxRule.builder()
                .ruleId("RULE015")
                .isin("DE0007164600")
                .marketCountryCode("DE")
                .clientType(ClientType.RETAIL)
                .clientDomicile("DE")
                .statutoryRatePercent(new BigDecimal("0"))
                .treatyRatePercent(new BigDecimal("0"))
                .treatyApplicableToManufactured(false)
                .build(),
            
            // BMW Rules
            // PENSION_FUND + NO domicile: full exemption
            WithholdingTaxRule.builder()
                .ruleId("RULE016")
                .isin("DE0005190003")
                .marketCountryCode("DE")
                .clientType(ClientType.PENSION_FUND)
                .clientDomicile("NO")
                .statutoryRatePercent(new BigDecimal("25"))
                .treatyRatePercent(new BigDecimal("0"))
                .treatyApplicableToManufactured(false)
                .build(),
            
            // ASSET_MANAGER + US domicile: 15% treaty rate
            WithholdingTaxRule.builder()
                .ruleId("RULE017")
                .isin("DE0005190003")
                .marketCountryCode("DE")
                .clientType(ClientType.ASSET_MANAGER)
                .clientDomicile("US")
                .statutoryRatePercent(new BigDecimal("25"))
                .treatyRatePercent(new BigDecimal("15"))
                .treatyApplicableToManufactured(true)
                .build()
        );
    }
}
