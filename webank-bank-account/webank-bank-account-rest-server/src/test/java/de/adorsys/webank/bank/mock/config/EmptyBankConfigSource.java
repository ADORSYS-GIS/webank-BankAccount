/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.mock.config;

import java.util.Collections;
import java.util.List;

import de.adorsys.webank.bank.api.service.domain.ASPSPConfigData;
import de.adorsys.webank.bank.api.service.domain.ASPSPConfigSource;
import de.adorsys.webank.bank.api.service.domain.LedgerAccountModel;

public class EmptyBankConfigSource implements ASPSPConfigSource {

    @Override
    public ASPSPConfigData aspspConfigData() {
        return new ASPSPConfigData();
    }

    @Override
    public List<LedgerAccountModel> chartOfAccount(String coaFile) {
        return Collections.emptyList();
    }

}
