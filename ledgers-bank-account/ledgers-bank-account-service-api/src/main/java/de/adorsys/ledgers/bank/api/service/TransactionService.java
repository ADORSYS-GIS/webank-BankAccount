/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.bank.api.service;

import de.adorsys.ledgers.bank.api.domain.MockBookingDetailsBO;

import java.util.List;
import java.util.Map;

public interface TransactionService {
    Map<String, String> bookMockTransaction(List<MockBookingDetailsBO> trDetails);
}
