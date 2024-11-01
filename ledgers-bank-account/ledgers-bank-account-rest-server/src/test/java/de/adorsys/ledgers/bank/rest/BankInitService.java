/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.bank.rest;

import de.adorsys.ledgers.bank.api.service.BankAccountInitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankInitService {
    private final BankAccountInitService bankAccountInitService;

    // necessary as for creating a bank account, yo need a pre initialized chart of account.
    public void init() {
        bankAccountInitService.initConfigData();
    }
}
