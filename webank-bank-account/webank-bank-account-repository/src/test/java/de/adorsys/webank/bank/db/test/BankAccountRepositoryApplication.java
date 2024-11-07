/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.db.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import de.adorsys.webank.bank.db.EnableBankAccountRepository;

@SpringBootApplication
@EnableBankAccountRepository
@EnableJpaAuditing
public class BankAccountRepositoryApplication {
	public static void main(String[] args) {
		SpringApplication.run(BankAccountRepositoryApplication.class, args);
	}
}
