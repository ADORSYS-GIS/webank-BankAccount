/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.bank.api.service;

import de.adorsys.ledgers.bank.api.domain.BankAccountBO;
import de.adorsys.ledgers.bank.api.domain.BankAccountDetailsBO;
import de.adorsys.ledgers.bank.api.domain.FundsConfirmationRequestBO;
import de.adorsys.ledgers.bank.api.domain.TransactionDetailsBO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;//NOPMD

public interface BankAccountService {

    /**
     * Retrieve accounts by IBAN and Currency(partial/empty)
     *
     * @param iban     mandatory IBAN
     * @param currency optional or partial Currency
     * @return List of accounts as if currency not specified can be many
     */
    List<BankAccountBO> getAccountsByIbanAndParamCurrency(String iban, String currency);

    /**
     * Retrieve account by IBAN and Currency
     *
     * @param iban     IBAN
     * @param currency Currency
     * @return BankAccount
     */
    BankAccountBO getAccountByIbanAndCurrency(String iban, Currency currency);

    /**
     * Retrieve account by accountId
     *
     * @param accountId the account id
     * @return BankAccount
     */
    BankAccountBO getAccountById(String accountId);

    Optional<BankAccountBO> getOptionalAccountByIbanAndCurrency(String iban, Currency currency);

    Optional<BankAccountBO> getOptionalAccountById(String accountId);

    BankAccountBO createNewAccount(BankAccountBO bankAccountBO, String userName, String branch);

    BankAccountDetailsBO getAccountDetailsByIbanAndCurrency(String iban, Currency currency, LocalDateTime refTime, boolean withBalances);

    BankAccountDetailsBO getAccountDetailsById(String accountId, LocalDateTime refTime, boolean withBalances);

    TransactionDetailsBO getTransactionById(String accountId, String transactionId);

    List<TransactionDetailsBO> getTransactionsByDates(String accountId, LocalDateTime dateFrom, LocalDateTime dateTo);

    Page<TransactionDetailsBO> getTransactionsByDatesPaged(String accountId, LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);

    boolean confirmationOfFunds(FundsConfirmationRequestBO requestBO);

    String readIbanById(String id);

    List<BankAccountDetailsBO> findDetailsByBranch(String branch);

    Page<BankAccountDetailsBO> findDetailsByBranchPaged(String branch, String queryParam, boolean withBalance, Pageable pageable);

    void changeAccountsBlockedStatus(String userId, boolean isSystemBlock, boolean lockStatusToSet);

    Page<BankAccountBO> findByBranchIdsAndMultipleParams(Collection<String> branchIds, String iban, Boolean blocked, Pageable pageable);

    void changeAccountsBlockedStatus(Set<String> accountIds, boolean systemBlock, boolean lockStatusToSet);

    void changeCreditLimit(String accountId, BigDecimal creditLimit);
}
