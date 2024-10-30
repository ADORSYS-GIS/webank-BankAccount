/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.bank.api.service;

/**
 * Start initialization of bank account module after environment initialized.
 * 
 * @author fpo
 *
 */
public interface BankAccountInitService {

	void initConfigData();

}
