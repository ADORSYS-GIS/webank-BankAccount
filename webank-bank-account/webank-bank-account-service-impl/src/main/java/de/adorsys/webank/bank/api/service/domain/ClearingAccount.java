/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.service.domain;

import lombok.Data;

@Data
public class ClearingAccount {
	private String accountNbr;
	private String paymentProduct;
}
