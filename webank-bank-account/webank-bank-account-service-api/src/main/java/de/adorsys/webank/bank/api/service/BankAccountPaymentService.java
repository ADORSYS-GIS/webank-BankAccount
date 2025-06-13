/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.service;

import de.adorsys.webank.bank.api.domain.PaymentBO;
import de.adorsys.webank.bank.api.domain.PaymentTypeBO;
import de.adorsys.webank.bank.api.domain.TransactionStatusBO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface BankAccountPaymentService {

    TransactionStatusBO getPaymentStatusById(String paymentId);

    PaymentBO getPaymentById(String paymentId);

    PaymentBO initiatePayment(PaymentBO paymentBO, TransactionStatusBO status);

    TransactionStatusBO executePayment(String paymentId, String userName);

    TransactionStatusBO cancelPayment(String paymentId);

    String readIbanByPaymentId(String paymentId);

    TransactionStatusBO updatePaymentStatus(String paymentId, TransactionStatusBO status);

    List<PaymentBO> getPaymentsByTypeStatusAndDebtor(PaymentTypeBO paymentType, TransactionStatusBO status, Set<String> accountIds);

    Page<PaymentBO> getPaymentsByTypeStatusAndDebtorPaged(PaymentTypeBO paymentType, TransactionStatusBO status, Set<String> accountIds, Pageable pageable);

    Page<PaymentBO> getPaymentsByTypeStatusAndDebtorInPaged(Set<PaymentTypeBO> paymentType, Set<TransactionStatusBO> status, Set<String> accountIds, Pageable pageable);

    boolean existingTargetById(String paymentTargetId);

    boolean existingPaymentById(String paymentId);
}
