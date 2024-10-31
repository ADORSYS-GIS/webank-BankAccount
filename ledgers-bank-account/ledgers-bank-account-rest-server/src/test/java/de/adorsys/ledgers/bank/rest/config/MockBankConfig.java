/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */
package de.adorsys.ledgers.bank.rest.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.adorsys.ledgers.bank.api.service.domain.ASPSPConfigSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Principal;

@Configuration
public class MockBankConfig {
    @Bean
    public ASPSPConfigSource configSource() {
        return new MockBankConfigSource();
    }

    @Bean
    public Principal getPrincipal() {
        return () -> "anonymous";
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }
}
