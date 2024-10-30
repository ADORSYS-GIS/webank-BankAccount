/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.bank.api.service.impl;

import de.adorsys.ledgers.bank.api.service.BankAccountConfigService;
import de.adorsys.ledgers.postings.api.domain.LedgerAccountBO;
import de.adorsys.ledgers.postings.api.domain.LedgerBO;
import de.adorsys.ledgers.postings.api.service.LedgerService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractServiceImpl {
    protected final BankAccountConfigService bankAccountConfigService;
    protected final LedgerService ledgerService;

    protected LedgerBO loadLedger() {
        String ledgerName = bankAccountConfigService.getLedger();
        return ledgerService.findLedgerByName(ledgerName)
                       .orElseThrow(() -> new IllegalStateException(String.format("Ledger with name %s not found", ledgerName)));
    }

    protected LedgerAccountBO loadClearingAccount(LedgerBO ledgerBO, String paymentProductBO) {
        String clearingAccount = bankAccountConfigService.getClearingAccount(paymentProductBO);
        return ledgerService.findLedgerAccount(ledgerBO, clearingAccount);
    }
}
