/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.domain.payment;

import lombok.Data;

@Data
public class RemittanceInformationStructuredTO {
    private String reference;
    private String referenceType;
    private String referenceIssuer;
}
