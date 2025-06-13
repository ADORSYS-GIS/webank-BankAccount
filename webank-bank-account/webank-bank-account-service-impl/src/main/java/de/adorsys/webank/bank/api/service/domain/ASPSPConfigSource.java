/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.service.domain;

import java.util.List;

public interface ASPSPConfigSource {
	ASPSPConfigData aspspConfigData();
	List<LedgerAccountModel> chartOfAccount(String coaFile);
}
