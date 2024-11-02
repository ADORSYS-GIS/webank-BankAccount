/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */
package de.adorsys.ledgers.bank.mock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.adorsys.ledgers.bank.api.service.domain.ASPSPConfigSource;

@Configuration
public class EmptyBankConfig {
    @Bean
    public ASPSPConfigSource configSource() {
        return new EmptyBankConfigSource();
    }
}
