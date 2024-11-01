/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.bank.api.service.impl.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import de.adorsys.ledgers.bank.api.service.EnableBankAccountService;
import de.adorsys.ledgers.bank.server.utils.client.ExchangeRateClient;
import de.adorsys.ledgers.postings.impl.EnablePostingService;

@SpringBootApplication
@EnableBankAccountService
@EnablePostingService
@EnableJpaAuditing
@EnableFeignClients(basePackageClasses = ExchangeRateClient.class)
public class BankAccountServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(BankAccountServiceApplication.class, args);
	}
}
