/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.bank.api.service;

import de.adorsys.ledgers.bank.api.domain.AmountBO;
import de.adorsys.ledgers.bank.api.domain.PaymentBO;

import java.time.LocalDateTime;

/**
 * Proceed with booking of payments orders.
 *
 * @author fpo
 */
public interface BankAccountTransactionService {

    void bookPayment(PaymentBO payment, LocalDateTime postingTime, String userName);

    void depositCash(String accountId, AmountBO amount, String user);
}
