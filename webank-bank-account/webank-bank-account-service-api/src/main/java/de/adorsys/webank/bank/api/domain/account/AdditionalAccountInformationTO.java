/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.domain.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdditionalAccountInformationTO {
    private String accountOwnerName;
    private Integer scaWeight;
}
