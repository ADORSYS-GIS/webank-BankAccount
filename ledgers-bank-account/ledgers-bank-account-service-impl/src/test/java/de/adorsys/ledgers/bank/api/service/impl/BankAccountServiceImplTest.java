/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.bank.api.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import de.adorsys.ledgers.bank.api.domain.*;//NOPMD
import de.adorsys.ledgers.bank.api.service.BankAccountConfigService;
import de.adorsys.ledgers.bank.api.service.mappers.TransactionDetailsMapper;
import de.adorsys.ledgers.bank.db.domain.AccountType;
import de.adorsys.ledgers.bank.db.domain.AccountUsage;
import de.adorsys.ledgers.bank.db.domain.BankAccount;
import de.adorsys.ledgers.bank.db.repository.BankAccountRepository;
import de.adorsys.ledgers.postings.api.domain.AccountStmtBO;
import de.adorsys.ledgers.postings.api.domain.LedgerAccountBO;
import de.adorsys.ledgers.postings.api.domain.LedgerBO;
import de.adorsys.ledgers.postings.api.domain.PostingLineBO;
import de.adorsys.ledgers.postings.api.service.AccountStmtService;
import de.adorsys.ledgers.postings.api.service.LedgerService;
import de.adorsys.ledgers.postings.api.service.PostingService;
import de.adorsys.ledgers.util.exception.DepositModuleException;
import de.adorsys.ledgers.util.exception.PostingModuleException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import pro.javatar.commons.reader.YamlReader;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static de.adorsys.ledgers.util.exception.PostingErrorCode.POSTING_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;//NOPMD
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;//NOPMD

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.TooManyMethods")
class BankAccountServiceImplTest {
    private static final String ACCOUNT_ID = "ACCOUNT_ID";
    private static final String POSTING_ID = "posting_ID";
    private static final String SYSTEM = "System";
    private static final String USER_ID = "123";
    private static final LocalDateTime CREATED = LocalDateTime.now();
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final Currency EUR = Currency.getInstance("EUR");

    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private LedgerService ledgerService;
    @Mock
    private BankAccountConfigService bankAccountConfigService;
    @Mock
    private PostingService postingService;
    @Mock
    private TransactionDetailsMapper transactionDetailsMapper;
    @Mock
    private AccountStmtService accountStmtService;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    private static final ObjectMapper STATIC_MAPPER = new ObjectMapper()
                                                              .findAndRegisterModules()
                                                              .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                                                              .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                                                              .configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false)
                                                              .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                                                              .registerModule(new Jdk8Module())
                                                              .registerModule(new JavaTimeModule())
                                                              .registerModule(new ParameterNamesModule());

    @Test
    void createBankAccount() {
        // Given
        when(bankAccountConfigService.getBankParentAccount()).thenReturn(getLedgerAccountBO().getName());
        when(ledgerService.newLedgerAccount(any(), anyString())).thenReturn(getLedgerAccountBO());
        when(bankAccountRepository.save(any())).thenReturn(getBankAccount(false, null));
        when(ledgerService.findLedgerByName(any())).thenReturn(Optional.of(getLedger()));

        // When
        BankAccountBO BankAccount = bankAccountService.createNewAccount(getBankAccountBO(), SYSTEM, "");

        // Then
        assertNotNull(BankAccount);
        assertFalse(BankAccount.getId().isEmpty());
    }

    @Test
    void createBankAccount_account_already_exist() {
        // Given
        when(bankAccountRepository.findByIbanAndCurrency(anyString(), anyString())).thenReturn(Optional.of(getBankAccount(false, null)));
        BankAccountBO BankAccountBO = getBankAccountBO();
        // Then
        assertThrows(DepositModuleException.class, () -> bankAccountService.createNewAccount(BankAccountBO, SYSTEM, ""));
    }

    @Test
    void getBankAccountById() {
        // Given
        when(bankAccountRepository.findById(any())).thenReturn(Optional.of(getBankAccount(false, null)));
        // When
        BankAccountDetailsBO BankAccountDetailsBO = bankAccountService.getAccountDetailsById("id", NOW, false);
        // Then
        assertNotNull(BankAccountDetailsBO);
        assertNotNull(BankAccountDetailsBO.getAccount());
    }

    @Test
    void getBankAccountById_wrong_id() {
        // Given
        when(bankAccountRepository.findById("wrong_id")).thenReturn(Optional.empty());

        // Then
        assertThrows(DepositModuleException.class, () -> bankAccountService.getAccountDetailsById("wrong_id", NOW, false));
    }

    @Test
    void getBankAccountByIBANAndCurrency() {
        // Given
        when(bankAccountRepository.findByIbanAndCurrency(any(), any())).thenReturn(Optional.of(getBankAccount(false, null)));
        // When
        BankAccountDetailsBO accountDetailsBO = bankAccountService.getAccountDetailsByIbanAndCurrency("iban", EUR, NOW, false);
        // Then
        assertNotNull(accountDetailsBO);
        assertNotNull(accountDetailsBO.getAccount());
    }

    @Test
    void checkAccountStatus_enabled() {
        // Given
        when(bankAccountRepository.findByIbanAndCurrency(any(), any())).thenReturn(Optional.of(getBankAccount(false, null)));
        // When
        BankAccountDetailsBO accountDetailsBO = bankAccountService.getAccountDetailsByIbanAndCurrency("iban", EUR, NOW, false);
        boolean checkAccountStatus = accountDetailsBO.isEnabled();

        // Then
        assertNotNull(accountDetailsBO);
        assertNotNull(accountDetailsBO.getAccount());
        assertTrue(checkAccountStatus);
    }

    @Test
    void checkAccountStatus_blocked() {
        // Given
        when(bankAccountRepository.findByIbanAndCurrency(any(), any())).thenReturn(Optional.of(getBankAccount(true, null)));
        // When
        BankAccountDetailsBO accountDetailsBO = bankAccountService.getAccountDetailsByIbanAndCurrency("iban", EUR, NOW, false);
        boolean checkAccountStatus = accountDetailsBO.isEnabled();

        // Then
        assertNotNull(accountDetailsBO);
        assertNotNull(accountDetailsBO.getAccount());
        assertFalse(checkAccountStatus);
    }

    @Test
    void checkAccountStatus_deleted() {
        // Given
        when(bankAccountRepository.findByIbanAndCurrency(any(), any())).thenReturn(Optional.of(getBankAccount(true, null)));
        // When
        BankAccountDetailsBO accountDetailsBO = bankAccountService.getAccountDetailsByIbanAndCurrency("iban", EUR, NOW, false);
        boolean checkAccountStatus = accountDetailsBO.isEnabled();

        // Then
        assertNotNull(accountDetailsBO);
        assertNotNull(accountDetailsBO.getAccount());
        assertFalse(checkAccountStatus);
    }

    @Test
    void findBankAccountsByBranch() {
        // Given
        when(bankAccountRepository.findByBranch(any())).thenReturn(Collections.singletonList(getBankAccount(false, null)));
        // When
        List<BankAccountDetailsBO> accounts = bankAccountService.findDetailsByBranch(anyString());

        // Then
        // account detail collection is not null
        assertNotNull(accounts);
        // account detail collection is not empty
        assertFalse(accounts.isEmpty());
    }

    @Test
    void getTransactionById() {
        // Given
        when(bankAccountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(readFile(BankAccount.class, "BankAccount.yml")));
        when(postingService.findPostingLineById(any(), any())).thenReturn(new PostingLineBO());
        when(transactionDetailsMapper.toTransactionSigned(any())).thenReturn(readFile(TransactionDetailsBO.class, "Transaction.yml"));
        // When
        TransactionDetailsBO result = bankAccountService.getTransactionById(ACCOUNT_ID, POSTING_ID);

        // Then
        assertNotNull(result);
        assertEquals(readFile(TransactionDetailsBO.class, "Transaction.yml"), result);
    }

    @Test
    void getTransactionById_Failure() {
        // Given
        when(bankAccountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(readFile(BankAccount.class, "BankAccount.yml")));
        when(postingService.findPostingLineById(any(), any())).thenThrow(PostingModuleException.builder()
                                                                                 .errorCode(POSTING_NOT_FOUND)
                                                                                 .devMsg(String.format("Could not find posting by ac id: %s and posting id: %s", ACCOUNT_ID, POSTING_ID))
                                                                                 .build());
        // Then
        assertThrows(PostingModuleException.class, () -> bankAccountService.getTransactionById(ACCOUNT_ID, POSTING_ID));
    }

    @Test
    void getTransactionsByDates() throws JsonProcessingException {
        // Given
        when(bankAccountRepository.findById(any())).thenReturn(Optional.of(new BankAccount()));
        when(postingService.findPostingsByDates(any(), any(), any())).thenReturn(Collections.singletonList(newPostingLineBO()));
        when(transactionDetailsMapper.toTransactionSigned(any())).thenReturn(readFile(TransactionDetailsBO.class, "Transaction.yml"));

        // When
        List<TransactionDetailsBO> result = bankAccountService.getTransactionsByDates(ACCOUNT_ID, LocalDateTime.of(2018, 12, 12, 0, 0), LocalDateTime.of(2018, 12, 18, 0, 0));

        // Then
        assertFalse(result.isEmpty());
    }

    @Test
    void confirmationOfFunds_more_than_necessary_available() throws NoSuchFieldException {
        confirmationOfFunds_more_than_necessary_available(100, BigDecimal.ZERO);
        confirmationOfFunds_more_than_necessary_available(101, BigDecimal.ZERO);
    }

    @Test
    void confirmationOfFunds_more_than_necessary_available_credit_enabled() throws NoSuchFieldException {
        confirmationOfFunds_more_than_necessary_available(90, BigDecimal.valueOf(10));
        confirmationOfFunds_more_than_necessary_available(91, BigDecimal.valueOf(10));
    }

    @Test
    void confirmationOfFunds_Failure_credit_enabled() {
        when(bankAccountRepository.findByIbanAndCurrency(any(), any())).thenReturn(Optional.of(getBankAccount(false, BigDecimal.TEN)));
        when(accountStmtService.readStmt(any(), any())).thenReturn(newAccountStmtBO(50));
        when(ledgerService.findLedgerByName(any())).thenReturn(Optional.of(getLedger()));

        ReflectionTestUtils.setField(bankAccountService, "exchangeRatesService", new CurrencyExchangeRatesServiceImpl(null, null));

        FundsConfirmationRequestBO requestBO = readFile(FundsConfirmationRequestBO.class, "FundsConfirmationRequest.yml");
        // Then
        boolean result = bankAccountService.confirmationOfFunds(requestBO);
        assertFalse(result);
    }

    @Test
    void confirmationOfFunds_Failure() {
        FundsConfirmationRequestBO requestBO = readFile(FundsConfirmationRequestBO.class, "FundsConfirmationRequest.yml");
        // Then
        assertThrows(DepositModuleException.class, () -> bankAccountService.confirmationOfFunds(requestBO));
    }

    @Test
    void getAccountsByIbanAndParamCurrency() {
        when(bankAccountRepository.findAllByIbanAndCurrencyContaining(anyString(), anyString())).thenReturn(Collections.singletonList(getBankAccount(false, BigDecimal.ZERO)));
        List<BankAccountBO> result = bankAccountService.getAccountsByIbanAndParamCurrency("iban", "EUR");
        assertEquals(Collections.singletonList(getBankAccountBO()), result);
    }

    @Test
    void getAccountByIbanAndCurrency() {
        // Given
        when(bankAccountRepository.findByIbanAndCurrency(anyString(), anyString())).thenReturn(Optional.of(getBankAccount(false, BigDecimal.ZERO)));
        // When
        BankAccountBO result = bankAccountService.getAccountByIbanAndCurrency("iban", EUR);

        // Then
        assertEquals(getBankAccountBO(), result);
    }

    @Test
    void getAccountByIbanAndCurrency_not_found() {
        // Given
        when(bankAccountRepository.findByIbanAndCurrency(anyString(), anyString())).thenReturn(Optional.empty());

        // Then
        assertThrows(DepositModuleException.class, () -> bankAccountService.getAccountByIbanAndCurrency("iban", EUR));
    }

    @Test
    void getAccountById() {
        // Given
        when(bankAccountRepository.findById(anyString())).thenReturn(Optional.of(getBankAccount(false, BigDecimal.ZERO)));
        // When
        BankAccountBO result = bankAccountService.getAccountById("accountId");

        // Then
        assertEquals(getBankAccountBO(), result);
    }

    @Test
    void getAccountById_not_found() {
        // Given
        when(bankAccountRepository.findById(anyString())).thenReturn(Optional.empty());

        // Then
        assertThrows(DepositModuleException.class, () -> bankAccountService.getAccountById("accountId"));
    }

    @Test
    void getTransactionsByDatesPaged() throws JsonProcessingException {
        // Given
        when(bankAccountRepository.findById(anyString())).thenReturn(Optional.of(getBankAccount(false, null)));
        when(postingService.findPostingsByDatesPaged(any(), any(), any(), any())).thenReturn(new PageImpl<>(Collections.singletonList(newPostingLineBO())));
        when(transactionDetailsMapper.toTransactionSigned(any())).thenReturn(readFile(TransactionDetailsBO.class, "Transaction.yml"));
        // When
        Page<TransactionDetailsBO> result = bankAccountService.getTransactionsByDatesPaged("accountId", NOW, NOW, Pageable.unpaged());

        // Then
        assertEquals(new PageImpl<>(Collections.singletonList(readFile(TransactionDetailsBO.class, "Transaction.yml"))), result);
    }

    @Test
    void findDetailsByBranchPaged() {
        // Given
        when(bankAccountRepository.findByBranchAndIbanContaining(anyString(), anyString(), any())).thenReturn(new PageImpl<>(Collections.singletonList(getBankAccount(false, null))));
        // When
        Page<BankAccountDetailsBO> result = bankAccountService.findDetailsByBranchPaged("branchId", "someParam", false, Pageable.unpaged());

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
    }

    @Test
    void readIbanById() {
        // Given
        BankAccount account = new BankAccount();
        account.setIban("DE123456789");
        when(bankAccountRepository.findById(anyString())).thenReturn(Optional.of(account));

        // When
        String result = bankAccountService.readIbanById(ACCOUNT_ID);

        // Then
        assertEquals("DE123456789", result);
    }

    private void confirmationOfFunds_more_than_necessary_available(long amount, BigDecimal creditAmount) {
        when(bankAccountRepository.findByIbanAndCurrency(any(), any())).thenReturn(Optional.of(getBankAccount(false, creditAmount)));
        when(accountStmtService.readStmt(any(), any())).thenReturn(newAccountStmtBO(amount));
        when(ledgerService.findLedgerByName(any())).thenReturn(Optional.of(getLedger()));

        ReflectionTestUtils.setField(bankAccountService, "exchangeRatesService", new CurrencyExchangeRatesServiceImpl(null, null));

        boolean response = bankAccountService.confirmationOfFunds(readFile(FundsConfirmationRequestBO.class, "FundsConfirmationRequest.yml"));
        assertTrue(response);
    }

    private PostingLineBO newPostingLineBO() throws JsonProcessingException {
        PostingLineBO pl = new PostingLineBO();
        pl.setAccount(new LedgerAccountBO());
        pl.setDetails(STATIC_MAPPER.writeValueAsString(new TransactionDetailsBO()));
        return pl;
    }

    private BankAccount getBankAccount(boolean status, BigDecimal creditAmount) {
        return new BankAccount("id", "iban", "msisdn", "EUR",
                                  "name", "displayName", "product", null, AccountType.CASH, "bic", "linked",
                                  AccountUsage.PRIV, "details", status, false, CREATED, creditAmount);
    }

    private BankAccountBO getBankAccountBO() {
        return BankAccountBO.builder().id("id")
                       .iban("iban").msisdn("msisdn")
                       .currency(EUR)
                       .name("name").product("product")
                       .accountType(AccountTypeBO.CASH)
                       .bic("bic")
                       .usageType(AccountUsageBO.PRIV).details("details")
                       .linkedAccounts("linked")
                       .displayName("displayName")
                       .created(CREATED)
                       .creditLimit(BigDecimal.ZERO)
                       .build();
    }

    private LedgerAccountBO getLedgerAccountBO() {
        LedgerAccountBO bo = new LedgerAccountBO();
        bo.setId("id");
        bo.setName("name");

        return bo;
    }

    private static LedgerBO getLedger() {
        LedgerBO ledgerBO = new LedgerBO();
        ledgerBO.setName("ledger");
        return ledgerBO;
    }

    private AccountStmtBO newAccountStmtBO(long amount) {
        AccountStmtBO result = new AccountStmtBO();
        LedgerAccountBO ledgerAccountBO2 = readFile(LedgerAccountBO.class, "LedgerAccount.yml");
        result.setAccount(ledgerAccountBO2);
        result.setPstTime(NOW);
        result.setTotalCredit(BigDecimal.valueOf(amount));
        return result;
    }

    private <T> T readFile(Class<T> t, String file) {
        try {
            return YamlReader.getInstance().getObjectFromResource(BankAccountPaymentServiceImpl.class, file, t);
        } catch (IOException e) {
            e.printStackTrace(); //NOPMD
            throw new IllegalStateException("Resource file not found", e);
        }
    }

    @Test
    void changeAccountsBlockedStatus_system_block() {
        bankAccountService.changeAccountsBlockedStatus(USER_ID, true, true);
        verify(bankAccountRepository, times(1)).updateSystemBlockedStatus(USER_ID, true);
    }

    @Test
    void changeAccountsBlockedStatus_regular_block() {
        bankAccountService.changeAccountsBlockedStatus(USER_ID, false, true);
        verify(bankAccountRepository, times(1)).updateBlockedStatus(USER_ID, true);
    }

    @Test
    void changeAccountsBlockedStatus_list_system_block() {
        // When
        bankAccountService.changeAccountsBlockedStatus(Collections.singleton(USER_ID), true, true);

        // Then
        verify(bankAccountRepository, times(1)).updateSystemBlockedStatus(Collections.singleton(USER_ID), true);
    }

    @Test
    void changeAccountsBlockedStatus_list_regular_block() {
        // When
        bankAccountService.changeAccountsBlockedStatus(Collections.singleton(USER_ID), false, true);

        // Then
        verify(bankAccountRepository, times(1)).updateBlockedStatus(Collections.singleton(USER_ID), true);
    }

    @Test
    void changeCreditLimit() {
        BankAccount BankAccount = getBankAccount(true, BigDecimal.TEN);
        when(bankAccountRepository.findById(any())).thenReturn(Optional.of(BankAccount));
        bankAccountService.changeCreditLimit(ACCOUNT_ID, BigDecimal.ONE);
        assertEquals(0, BankAccount.getCreditLimit().compareTo(BigDecimal.ONE));
    }

}
