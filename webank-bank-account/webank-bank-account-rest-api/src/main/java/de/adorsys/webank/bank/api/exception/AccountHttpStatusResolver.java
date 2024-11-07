/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.exception;

import static de.adorsys.webank.bank.api.exception.AccountErrorCode.COULD_NOT_EXECUTE_STATEMENT;
import static de.adorsys.webank.bank.api.exception.AccountErrorCode.CURRENCY_NOT_SUPPORTED;
import static de.adorsys.webank.bank.api.exception.AccountErrorCode.DEPOSIT_ACCOUNT_EXISTS;
import static de.adorsys.webank.bank.api.exception.AccountErrorCode.DEPOSIT_ACCOUNT_NOT_FOUND;
import static de.adorsys.webank.bank.api.exception.AccountErrorCode.DEPOSIT_OPERATION_FAILURE;
import static de.adorsys.webank.bank.api.exception.AccountErrorCode.INSUFFICIENT_FUNDS;
import static de.adorsys.webank.bank.api.exception.AccountErrorCode.PAYMENT_NOT_FOUND;
import static de.adorsys.webank.bank.api.exception.AccountErrorCode.PAYMENT_PROCESSING_FAILURE;
import static de.adorsys.webank.bank.api.exception.AccountErrorCode.PAYMENT_WITH_ID_EXISTS;
import static de.adorsys.webank.bank.api.exception.AccountErrorCode.UNSUPPORTED_CREDIT_LIMIT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.EnumMap;

import org.springframework.http.HttpStatus;

public class AccountHttpStatusResolver {
    private static final EnumMap<AccountErrorCode, HttpStatus> container = new EnumMap<>(AccountErrorCode.class);

    private AccountHttpStatusResolver() {
    }

    static {
        //404 Block
        container.put(DEPOSIT_ACCOUNT_NOT_FOUND, NOT_FOUND);
        container.put(PAYMENT_NOT_FOUND, NOT_FOUND);

        //400 Block

        // This error is thrown when there is a failure in performing a deposit operation, such as adding or removing funds from an account.
        container.put(DEPOSIT_OPERATION_FAILURE, BAD_REQUEST);

        // This error is thrown when a currency that is not supported by the system is used in a transaction.
        container.put(CURRENCY_NOT_SUPPORTED, BAD_REQUEST);

        // This error is thrown when an unsupported credit limit is set, either exceeding the allowed limit or being invalid for the account.
        container.put(UNSUPPORTED_CREDIT_LIMIT, BAD_REQUEST);

        //403 Block
        // This error is thrown when there are insufficient funds in the deposit account to complete the requested transaction.
        container.put(INSUFFICIENT_FUNDS, FORBIDDEN);

        //417 Block

        container.put(COULD_NOT_EXECUTE_STATEMENT, EXPECTATION_FAILED);

        //500
        // This error is thrown when a failure occurs during the processing of a payment, typically due to server-side issues.
        container.put(PAYMENT_PROCESSING_FAILURE, INTERNAL_SERVER_ERROR);

        //409
        // This error is thrown when an attempt is made to process a payment with an ID that already exists in the system.
        container.put(PAYMENT_WITH_ID_EXISTS, CONFLICT);
        // This error is thrown when attempting to create a deposit account that already exists.
        container.put(DEPOSIT_ACCOUNT_EXISTS, CONFLICT);
    }
    public static HttpStatus resolveHttpStatusByCode(AccountErrorCode code) {
        return container.getOrDefault(code, BAD_REQUEST) ;
    }
}

