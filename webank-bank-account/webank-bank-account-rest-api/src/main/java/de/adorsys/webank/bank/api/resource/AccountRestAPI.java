/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.resource;

import static de.adorsys.webank.bank.api.utils.Constants.ACCOUNT_ID;
import static de.adorsys.webank.bank.api.utils.Constants.DATE_FROM_QUERY_PARAM;
import static de.adorsys.webank.bank.api.utils.Constants.DATE_TO_QUERY_PARAM;
import static de.adorsys.webank.bank.api.utils.Constants.LOCAL_DATE_YYYY_MM_DD_FORMAT;
import static de.adorsys.webank.bank.api.utils.Constants.PAGE;
import static de.adorsys.webank.bank.api.utils.Constants.SIZE;
import static de.adorsys.webank.bank.api.utils.Constants.TRANSACTION_ID;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import de.adorsys.webank.bank.api.domain.account.AccountBalanceTO;
import de.adorsys.webank.bank.api.domain.account.AccountDetailsTO;
import de.adorsys.webank.bank.api.domain.account.TransactionTO;
import de.adorsys.webank.bank.api.utils.CustomPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "LDG??? - Accounts", description = "Provides access to a deposit account.")
public interface AccountRestAPI {
    String BASE_PATH = "/accounts";

    @GetMapping("/{accountId}")
    @Operation(summary = "Load Account by AccountId",
            description = "Returns account details information for the given account id. ")
  
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountDetailsTO.class)),
                    description = "Account details.")
    })
    ResponseEntity<AccountDetailsTO> getAccountDetailsById(@Parameter(name = ACCOUNT_ID) @PathVariable(ACCOUNT_ID) String accountId);

    @GetMapping("/{accountId}/balances")
    @Operation(summary = "Read balances",
            description = "Returns balances of the deposit account with the given accountId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountBalanceTO.class)), description = "List of accounts balances for the given account.")
    })
    ResponseEntity<List<AccountBalanceTO>> getBalances(@Parameter(name = ACCOUNT_ID) @PathVariable(ACCOUNT_ID) String accountId);

    @GetMapping(path = "/{accountId}/transactions", params = {DATE_FROM_QUERY_PARAM, DATE_TO_QUERY_PARAM})
    @Operation(summary = "Find Transactions By Date", description = "Returns all transactions for the given account id")
    ResponseEntity<List<TransactionTO>> getTransactionByDates(
            @Parameter(name = ACCOUNT_ID)
            @PathVariable(ACCOUNT_ID) String accountId,
            @RequestParam(name = DATE_FROM_QUERY_PARAM, required = false) @DateTimeFormat(pattern = LOCAL_DATE_YYYY_MM_DD_FORMAT) LocalDateTime dateFrom,
            @RequestParam(name = DATE_TO_QUERY_PARAM) @DateTimeFormat(pattern = LOCAL_DATE_YYYY_MM_DD_FORMAT) LocalDateTime dateTo);

    @GetMapping(path = "/{accountId}/transactions/page", params = {DATE_FROM_QUERY_PARAM, DATE_TO_QUERY_PARAM, PAGE, SIZE})
    @Operation(summary = "Find Transactions By Date", description = "Returns transactions for the given account id for certain dates, paged view")
    ResponseEntity<CustomPage<TransactionTO>> getTransactionByDatesPaged(
            @Parameter(name = ACCOUNT_ID)
            @PathVariable(name = ACCOUNT_ID) String accountId,
            @RequestParam(name = DATE_FROM_QUERY_PARAM, required = false) @DateTimeFormat(pattern = LOCAL_DATE_YYYY_MM_DD_FORMAT) LocalDateTime dateFrom,
            @RequestParam(name = DATE_TO_QUERY_PARAM) @DateTimeFormat(pattern = LOCAL_DATE_YYYY_MM_DD_FORMAT) LocalDateTime dateTo,
            @RequestParam(PAGE) int page,
            @RequestParam(SIZE) int size);

    @GetMapping("/{accountId}/transactions/{transactionId}")
    @Operation(summary = "Load Transaction", description = "Returns the transaction with the given account id and transaction id.")
    ResponseEntity<TransactionTO> getTransactionById(
            @Parameter(name = ACCOUNT_ID)
            @PathVariable(name = ACCOUNT_ID) String accountId,
            @Parameter(name = TRANSACTION_ID)
            @PathVariable(name = TRANSACTION_ID) String transactionId);
        
}
