/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.test.empty;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import de.adorsys.webank.bank.api.service.EnableBankAccountService;
import de.adorsys.webank.bank.mock.config.EnableEmptyBank;
import de.adorsys.webank.bank.rest.EnableLedgersBankAccountRest;
import de.adorsys.webank.bank.server.utils.client.ExchangeRateClient;
import de.adorsys.ledgers.postings.impl.EnablePostingService;

@EnableScheduling
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableEmptyBank
@EnableBankAccountService
@EnablePostingService
@EnableLedgersBankAccountRest
@EnableFeignClients(basePackageClasses = ExchangeRateClient.class)
public class EmptyLedgersApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(EmptyLedgersApplication.class).run(args);
    }
}
