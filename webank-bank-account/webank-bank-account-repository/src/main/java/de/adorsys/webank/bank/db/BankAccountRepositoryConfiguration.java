/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.db;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import de.adorsys.webank.bank.db.domain.BankAccount;

@Configuration
@ComponentScan(basePackageClasses= {BankAccountDBBasePackage.class})
@EnableJpaRepositories
@EntityScan(basePackageClasses= {BankAccount.class, Jsr310JpaConverters.class})
public class BankAccountRepositoryConfiguration {
}
