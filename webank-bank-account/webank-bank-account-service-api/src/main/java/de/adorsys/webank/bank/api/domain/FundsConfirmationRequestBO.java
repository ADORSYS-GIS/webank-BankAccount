/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundsConfirmationRequestBO {
    private String psuId;
    private AccountReferenceBO psuAccount;
    private AmountBO instructedAmount;
    private String cardNumber;
    private String payee;
}
