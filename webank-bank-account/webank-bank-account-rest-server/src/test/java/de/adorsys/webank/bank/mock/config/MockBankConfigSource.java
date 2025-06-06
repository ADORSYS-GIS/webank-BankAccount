/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.mock.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.adorsys.webank.bank.api.service.domain.ASPSPConfigData;
import de.adorsys.webank.bank.api.service.domain.ASPSPConfigSource;
import de.adorsys.webank.bank.api.service.domain.LedgerAccountModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class MockBankConfigSource implements ASPSPConfigSource {
    private ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    @Override
    public ASPSPConfigData aspspConfigData() {
        InputStream inputStream = MockBankConfigSource.class.getResourceAsStream("aspsps-config.yml");
        try {
            return mapper.readValue(inputStream, ASPSPConfigData.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<LedgerAccountModel> chartOfAccount(String coaFile) {
        InputStream inputStream = MockBankConfigSource.class.getResourceAsStream(coaFile);
        LedgerAccountModel[] ledgerAccounts;
        try {
            ledgerAccounts = mapper.readValue(inputStream, LedgerAccountModel[].class);
        } catch (IOException e) {
            throw new IllegalStateException("Could not get resource", e);
        }
        return Arrays.asList(ledgerAccounts);
    }

}
