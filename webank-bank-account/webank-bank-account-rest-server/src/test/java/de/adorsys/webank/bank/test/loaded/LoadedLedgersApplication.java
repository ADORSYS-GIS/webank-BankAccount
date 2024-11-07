/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.test.loaded;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import de.adorsys.webank.bank.api.service.EnableBankAccountService;
import de.adorsys.webank.bank.mock.config.EnableMockBank;
import de.adorsys.webank.bank.rest.BankInitService;
import de.adorsys.webank.bank.rest.EnableLedgersBankAccountRest;
import de.adorsys.webank.bank.server.utils.client.ExchangeRateClient;
import de.adorsys.ledgers.postings.impl.EnablePostingService;
import de.adorsys.ledgers.util.EnableUtils;

@EnableScheduling
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableMockBank
@EnablePostingService
@EnableBankAccountService
@EnableUtils
@EnableLedgersBankAccountRest
@EnableFeignClients(basePackageClasses = ExchangeRateClient.class)
public class LoadedLedgersApplication implements ApplicationListener<ApplicationReadyEvent> {
    private final BankInitService bankInitService;
    private ExchangeRateClient exchangeRateClient;

    @Autowired
    public LoadedLedgersApplication(BankInitService bankInitService, ExchangeRateClient exchangeRateClient) {
        this.bankInitService = bankInitService;
        this.exchangeRateClient = exchangeRateClient;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(LoadedLedgersApplication.class).run(args);
    }

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
        exchangeRateClient.equals(null);
        bankInitService.init();
    }
}
