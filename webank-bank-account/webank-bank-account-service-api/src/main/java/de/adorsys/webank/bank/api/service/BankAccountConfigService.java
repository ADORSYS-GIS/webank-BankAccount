/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.service;

public interface BankAccountConfigService {

    String getBankParentAccount();

    String getLedger();

    String getClearingAccount(String paymentProduct);

    String getCashAccount();
}
