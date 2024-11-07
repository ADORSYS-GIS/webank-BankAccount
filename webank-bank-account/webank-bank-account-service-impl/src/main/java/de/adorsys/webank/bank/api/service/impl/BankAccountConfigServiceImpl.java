/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.service.impl;

import de.adorsys.webank.bank.api.service.BankAccountConfigService;
import de.adorsys.webank.bank.api.service.domain.ASPSPConfigData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountConfigServiceImpl implements BankAccountConfigService {
    private final ASPSPConfigData configData;

    @Override
    public String getBankParentAccount() {
        return configData.getBankParentAccount();
    }

    @Override
    public String getLedger() {
        return configData.getLedger();
    }

    @Override
    public String getClearingAccount(String paymentProduct) {
        return configData.getClearingAccount(paymentProduct);
    }

    @Override
    public String getCashAccount() {
        return configData.getCashAccount();
    }
}
