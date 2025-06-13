/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.domain;

import lombok.Data;

import java.util.List;

@Data
public class PaymentTargetDetailsBO extends TransactionDetailsBO {
    private AddressBO creditorAddress;
    /*Id of the referenced payment*/
    private String paymentOrderId;
    /*The type of the payment order.*/
    private PaymentTypeBO paymentType;
    private String paymentProduct;
    /*The transaction status*/
    private TransactionStatusBO transactionStatus;
    private String creditorAgent;
    private List<ExchangeRateBO> exchangeRate;
}
