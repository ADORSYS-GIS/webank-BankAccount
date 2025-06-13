/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.db.domain;

public enum ChargeBearer {
    CRED, //All transaction charges are to be borne by the creditor.
    DEBT, //All transaction charges are to be borne by the debtor.
    SHAR, //Shared In a credit transfer context, means that transaction charges on the sender side are to be borne by the debtor, transaction charges on the receiver side are to be borne by the creditor. In a direct debit context, means that transaction charges on the sender side are to be borne by the creditor, transaction charges on the receiver side are to be borne by the debtor.
    SLEV // Following Service Level Charges are to be applied following the rules agreed in the service level and/or scheme.
}
