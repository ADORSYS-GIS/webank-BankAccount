/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.service.impl;

import de.adorsys.webank.bank.db.domain.BankAccount;
import de.adorsys.webank.bank.db.domain.Payment;
import de.adorsys.webank.bank.db.repository.BankAccountRepository;
import de.adorsys.webank.bank.db.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.javatar.commons.reader.YamlReader;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;//NOPMD

@ExtendWith(MockitoExtension.class)
class PaymentExecutionSchedulerTest {

    private static final String ACCOUNT_ID = "accountId";
    private static final String IBAN = "DE91100000000123456789";
    private static final String SCHEDULER = "Scheduler";

    @InjectMocks
    private PaymentExecutionScheduler paymentExecutionScheduler;

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private BankAccountRepository accountRepository;
    @Mock
    private PaymentExecutionService executionService;

    @Test
    void scheduler_executed_successfully() {
        // Given
        Payment singlePayment = getSinglePayment();
        when(paymentRepository.getAllDuePayments())
                .thenReturn(Collections.singletonList(singlePayment));
        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.of(getBankAccount()));
        when(accountRepository.findByIbanAndCurrency(IBAN, "EUR"))
                .thenReturn(Optional.of(getBankAccount()));

        // When
        paymentExecutionScheduler.scheduler();

        // Then
        verify(executionService, times(1)).executePayment(singlePayment, SCHEDULER);
    }

    @Test
    void scheduler_not_executed_debtor_disabled() {
        // Given
        Payment singlePayment = getSinglePayment();
        when(paymentRepository.getAllDuePayments())
                .thenReturn(Collections.singletonList(singlePayment));
        BankAccount blockedAccount = getBankAccount();
        blockedAccount.setBlocked(true);

        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.of(blockedAccount));

        // When
        paymentExecutionScheduler.scheduler();

        // Then
        verify(executionService, never()).executePayment(singlePayment, SCHEDULER);
    }

    @Test
    void scheduler_not_executed_creditor_disabled() {
        // Given
        Payment singlePayment = getSinglePayment();
        when(paymentRepository.getAllDuePayments())
                .thenReturn(Collections.singletonList(singlePayment));
        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.of(getBankAccount()));

        BankAccount blockedAccount = getBankAccount();
        blockedAccount.setBlocked(true);

        when(accountRepository.findByIbanAndCurrency(IBAN, "EUR"))
                .thenReturn(Optional.of(blockedAccount));
        // When
        paymentExecutionScheduler.scheduler();

        // Then
        verify(executionService, never()).executePayment(singlePayment, SCHEDULER);
    }

    private BankAccount getBankAccount() {
        BankAccount activeAccount = new BankAccount();
        activeAccount.setBlocked(false);
        activeAccount.setSystemBlocked(false);

        return activeAccount;
    }

    private Payment getSinglePayment() {
        Payment payment = readFile(Payment.class, "PaymentSingle.yml");
        payment.getTargets().forEach(t -> t.setPayment(payment));
        return payment;
    }

    private <T> T readFile(Class<T> t, String file) {
        try {
            return YamlReader.getInstance().getObjectFromResource(BankAccountPaymentServiceImpl.class, file, t);
        } catch (IOException e) {
            e.printStackTrace(); //NOPMD
            throw new IllegalStateException("Resource file not found", e);
        }
    }
}