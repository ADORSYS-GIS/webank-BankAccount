/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.service.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.adorsys.webank.bank.api.domain.*;//NOPMD
import de.adorsys.webank.bank.api.service.impl.BankAccountServiceImpl;
import de.adorsys.webank.bank.db.domain.Payment;
import de.adorsys.ledgers.util.Ids;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.javatar.commons.reader.YamlReader;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
class PaymentMapperTest {

    private static final String SINGLE_PATH = "PaymentSingle.yml";
    private static final String BULK_PATH = "PaymentBulk.yml";
    private static final String TRANSACTION_ID = "TR_1";
    private static final String LINE_ID = "LINE_1";
    private static final LocalDate POSTING_DATE = LocalDate.now();
    private static final Currency EUR = Currency.getInstance("EUR");
    private final Payment SINGLE_PMT = readYml(Payment.class, SINGLE_PATH);
    private final PaymentBO SINGLE_PMT_BO = readYml(PaymentBO.class, SINGLE_PATH);
    private final Payment BULK_PMT = readYml(Payment.class, BULK_PATH);
    private final PaymentBO BULK_PMT_BO = readYml(PaymentBO.class, BULK_PATH);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private PaymentMapper mapper = Mappers.getMapper(PaymentMapper.class);

    @Test
    void toPayment_Single() {
        // When
        PaymentBO result = mapper.toPaymentBO(SINGLE_PMT);

        // Then
        assertNotNull(result);
        assertEquals(SINGLE_PMT_BO, result);
    }

    @Test
    void toPayment_Bulk() {
        // When
        PaymentBO result = mapper.toPaymentBO(BULK_PMT);

        // Then
        assertNotNull(result);
        assertEquals(BULK_PMT_BO, result);
    }

    @Test
    void toPaymentBO_Single() {
        // When
        Payment result = mapper.toPayment(SINGLE_PMT_BO);

        // Then
        assertNotNull(result);
        assertEquals(SINGLE_PMT, result);
    }

    @Test
    void toPaymentBO_Bulk() {
        // When
        Payment result = mapper.toPayment(BULK_PMT_BO);

        // Then
        assertNotNull(result);
        assertEquals(BULK_PMT, result);
    }

    @Test
    void toPaymentOrder() {
        // When
        PaymentOrderDetailsBO result = mapper.toPaymentOrder(SINGLE_PMT_BO);

        // Then
        assertNotNull(result);
        assertEquals(readYml(PaymentOrderDetailsBO.class, "PaymentOrderSingle.yml"), result);
    }

    @Test
    void toPaymentTargetDetails() {
        // When
        PaymentTargetDetailsBO result = mapper.toPaymentTargetDetails(TRANSACTION_ID, readYml(PaymentTargetBO.class, "PaymentTarget.yml"), LocalDate.of(2018, 12, 12), null, null);

        // Then
        assertNotNull(result);
        assertEquals(readYml(PaymentTargetDetailsBO.class, "PaymentTargetDetails.yml"), result);
    }

    @Test
    void toPaymentTargetDetailsBatch() {
        // Given
        PaymentBO payment = readYml(PaymentBO.class, "PaymentBulkBatchTrue.yml");
        AmountBO amount = new AmountBO(Currency.getInstance("EUR"), BigDecimal.valueOf(200));
        LocalDate date = LocalDate.of(2018, 12, 12);

        // When
        PaymentTargetDetailsBO result = mapper.toPaymentTargetDetailsBatch(TRANSACTION_ID, payment, amount, date, null, null, objectMapper);

        // Then
        assertNotNull(result);
        assertEquals(readYml(PaymentTargetDetailsBO.class, "PaymentTargetDetailsBatch.yml"), result);
    }

    @Test
    void trDetailsForDepositOperation() throws JsonProcessingException {
        // When
        TransactionDetailsBO result = mapper.toDepositTransactionDetails(new AmountBO(EUR, BigDecimal.TEN), getBankAccount(), new AccountReferenceBO(), POSTING_DATE, LINE_ID, null, objectMapper);

        TransactionDetailsBO expected = getDepositTrDetails();
        expected.setTransactionId(result.getTransactionId());

        // Then
        assertNotNull(result);
        assertNotNull(result.getTransactionId());
        assertEquals(expected, result);
    }

    private BankAccountBO getBankAccount() {
        return new BankAccountBO("id", "IBAN", null, null, null, null, EUR, "Anton Brueckner", null, null, null, null, null, null, null, false, false, "branch", null, BigDecimal.ZERO);
    }

    private TransactionDetailsBO getDepositTrDetails() throws JsonProcessingException {
        TransactionDetailsBO t = new TransactionDetailsBO();
        t.setTransactionId(Ids.id());
        t.setEndToEndId(LINE_ID);
        t.setBookingDate(POSTING_DATE);
        t.setValueDate(POSTING_DATE);
        t.setTransactionAmount(new AmountBO(EUR, BigDecimal.TEN));
        t.setCreditorAccount(new AccountReferenceBO());
        t.setCreditorName(getBankAccount().getName());
        t.setDebtorName(getBankAccount().getName());
        t.setDebtorAccount(getBankAccount().getReference());
        t.setBankTransactionCode("PMNT-MCOP-OTHR");
        t.setProprietaryBankTransactionCode("PMNT-MCOP-OTHR");
        t.setRemittanceInformationUnstructuredArray(objectMapper.writeValueAsBytes(Collections.singletonList("Cash deposit through Bank ATM")));
        return t;
    }

    private <T> T readYml(Class<T> t, String fileName) {
        try {
            return YamlReader.getInstance().getObjectFromResource(BankAccountServiceImpl.class, fileName, t);
        } catch (IOException e) {
            e.printStackTrace(); //NOPMD
            throw new IllegalStateException("Resource file not found", e);
        }
    }
}
