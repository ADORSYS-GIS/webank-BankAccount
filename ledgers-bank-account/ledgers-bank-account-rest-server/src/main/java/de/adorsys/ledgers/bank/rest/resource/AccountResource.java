/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.bank.rest.resource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.adorsys.ledgers.bank.api.domain.account.AccountBalanceTO;
import de.adorsys.ledgers.bank.api.domain.account.AccountDetailsTO;
import de.adorsys.ledgers.bank.api.domain.account.TransactionTO;
import de.adorsys.ledgers.bank.api.resource.AccountRestAPI;
import de.adorsys.ledgers.bank.api.service.BankAccountService;
import de.adorsys.ledgers.bank.api.utils.CustomPage;
import de.adorsys.ledgers.bank.rest.annotation.BankAccountUserResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@BankAccountUserResource
@RequiredArgsConstructor
@RequestMapping(AccountRestAPI.BASE_PATH)
public class AccountResource implements AccountRestAPI {

    private final BankAccountService bankAccountService;
    //private final MiddlewareUserManagementService userManagementService;

    @Override
    public ResponseEntity<AccountDetailsTO> getAccountDetailsById(String accountId) {
        return null;
    }

    @Override
    public ResponseEntity<List<AccountBalanceTO>> getBalances(String accountId) {
//        AccountDetailsTO accountDetails = bankAccountService.getAccountDetailsById(accountId, LocalDateTime.now(), true);
//        return ResponseEntity.ok(accountDetails.getBalances());
        return null;
    }

    @Override
    public ResponseEntity<List<TransactionTO>> getTransactionByDates(String accountId, LocalDateTime dateFrom, LocalDateTime dateTo) {
//        dateChecker(dateFrom, dateTo);
//        List<TransactionTO> transactions = bankAccountService.getTransactionsByDates(accountId, validDate(dateFrom), validDate(dateTo));
//        return ResponseEntity.ok(transactions);
        return null;
    }

    @Override
    public ResponseEntity<CustomPage<TransactionTO>> getTransactionByDatesPaged(String accountId, LocalDateTime dateFrom, LocalDateTime dateTo, int page, int size) {
//        dateChecker(dateFrom, dateTo);
//        CustomPageableImpl pageable = new CustomPageableImpl(page, size);
//        CustomPageImpl<TransactionTO> customPage = bankAccountService.getTransactionsByDatesPaged(accountId, dateFrom, dateTo, pageable);
//        return ResponseEntity.ok(customPage);
        return null;
    }

    @Override
    public ResponseEntity<TransactionTO> getTransactionById(String accountId, String transactionId) {
//        return ResponseEntity.ok(bankAccountService.getTransactionById(accountId, transactionId));
        return null;
    }

    private void dateChecker(LocalDateTime dateFrom, LocalDateTime dateTo) {
        if (!validDate(dateFrom).isEqual(validDate(dateTo))
                && validDate(dateFrom).isAfter(validDate(dateTo))) {
//            throw MiddlewareModuleException.builder()
//                    .errorCode(REQUEST_VALIDATION_FAILURE)
//                    .devMsg("Illegal request dates sequence, possibly swapped 'date from' with 'date to'")
//                    .build();
        }
    }

    private LocalDateTime validDate(LocalDateTime date) {
        return Optional.ofNullable(date)
                .orElseGet(LocalDateTime::now);
    }
}