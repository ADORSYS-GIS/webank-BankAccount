/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.db.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.math.BigDecimal;

@SuppressWarnings("java:S1700")
@Data
@Embeddable
public class Amount {
    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private BigDecimal amount;
}
