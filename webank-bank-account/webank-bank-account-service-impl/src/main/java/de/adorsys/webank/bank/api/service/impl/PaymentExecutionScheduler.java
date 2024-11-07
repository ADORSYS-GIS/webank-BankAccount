/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.service.impl;

import de.adorsys.webank.bank.db.domain.AccountReference;
import de.adorsys.webank.bank.db.domain.BankAccount;
import de.adorsys.webank.bank.db.domain.Payment;
import de.adorsys.webank.bank.db.domain.PaymentTarget;
import de.adorsys.webank.bank.db.repository.BankAccountRepository;
import de.adorsys.webank.bank.db.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentExecutionScheduler {
    private static final String SCHEDULER = "Scheduler";
    private final PaymentRepository paymentRepository;
    private final BankAccountRepository accountRepository;

    private final PaymentExecutionService executionService;

    @Scheduled(initialDelayString = "${ledgers.paymentScheduler.initialDelay}", fixedDelayString = "${ledgers.paymentScheduler.delay}")
    public void scheduler() {
        log.info("Payment Execution Scheduler started at {}", LocalDateTime.now());
        List<Payment> payments = paymentRepository.getAllDuePayments();
        payments.forEach(this::executeIfNotBlocked);
    }

    private void executeIfNotBlocked(Payment payment) {
        boolean debtorIsEnabled = isDebtorAccountEnabled(payment.getAccountId());
        boolean creditorsAreEnabled = areTargetsEnabled(payment.getTargets());

        if (debtorIsEnabled && creditorsAreEnabled) {
            executionService.executePayment(payment, SCHEDULER);
        }
    }

    private boolean isDebtorAccountEnabled(String accountId) {
        return accountRepository.findById(accountId)
                       .map(BankAccount::isEnabled)
                       .orElse(false);
    }

    private boolean areTargetsEnabled(List<PaymentTarget> targets) {
        return targets.stream()
                       .map(PaymentTarget::getCreditorAccount)
                       .allMatch(this::isEnabledCreditorAccount);
    }

    private boolean isEnabledCreditorAccount(AccountReference reference) {
        return accountRepository.findByIbanAndCurrency(reference.getIban(), reference.getCurrency())
                       .map(BankAccount::isEnabled)
                       .orElse(true);
    }
}
