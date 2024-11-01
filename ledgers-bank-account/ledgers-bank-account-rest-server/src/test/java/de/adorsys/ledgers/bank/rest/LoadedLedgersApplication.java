/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.bank.rest;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import de.adorsys.ledgers.bank.api.service.EnableBankAccountService;
import de.adorsys.ledgers.bank.server.utils.client.ExchangeRateClient;
import de.adorsys.ledgers.postings.impl.EnablePostingService;
import de.adorsys.ledgers.util.EnableUtils;

@EnableScheduling
@SpringBootApplication
@EnablePostingService
@EnableBankAccountService
@EnableUtils
@EnableLedgersBankAccountRest
@EnableFeignClients(basePackageClasses = ExchangeRateClient.class)
public class LoadedLedgersApplication implements ApplicationListener<ApplicationReadyEvent> {
    private final BankInitService bankInitService;
    private final Environment env;

    @Autowired
    public LoadedLedgersApplication(BankInitService bankInitService, Environment env) {
        this.bankInitService = bankInitService;
        this.env = env;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(LoadedLedgersApplication.class).run(args);
    }

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
        bankInitService.init();
    }
}
