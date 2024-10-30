/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.bank.api.service;

import de.adorsys.ledgers.bank.api.service.domain.ASPSPConfigData;
import de.adorsys.ledgers.bank.api.service.domain.ASPSPConfigSource;
import de.adorsys.ledgers.bank.db.EnableBankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = BankAccountServiceBasePackage.class,
        basePackages = "de.adorsys.ledgers.cleanup")
@EnableBankAccountRepository
public class BankAccountServiceConfiguration {

    private final ASPSPConfigSource configSource;

    @Autowired
    public BankAccountServiceConfiguration(ASPSPConfigSource configSource) {
        this.configSource = configSource;
    }

    @Bean
    public ASPSPConfigData configData() {
        return configSource.aspspConfigData();
    }
}
