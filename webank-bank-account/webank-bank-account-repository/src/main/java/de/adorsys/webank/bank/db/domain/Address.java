/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.db.domain;

import lombok.Data;

import jakarta.persistence.Embeddable;

@Data
@Embeddable
public class Address {
    private String street;
    private String buildingNumber;
    private String city;
    private String postalCode;
    private String country;

    private String line1;
    private String line2;
}
