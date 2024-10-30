/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.bank.db.repository;

import de.adorsys.ledgers.bank.db.domain.PaymentTarget;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PaymentTargetRepository extends PagingAndSortingRepository<PaymentTarget, String>, CrudRepository<PaymentTarget, String> {
}
