/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.bank.api.service.impl;

import de.adorsys.ledgers.bank.api.domain.*;//NOPMD
import de.adorsys.ledgers.bank.api.service.CurrencyExchangeRatesService;
import de.adorsys.ledgers.bank.api.service.BankAccountConfigService;
import de.adorsys.ledgers.bank.api.service.BankAccountService;
import de.adorsys.ledgers.bank.api.service.mappers.BankAccountMapper;
import de.adorsys.ledgers.bank.api.service.mappers.TransactionDetailsMapper;
import de.adorsys.ledgers.bank.db.domain.BankAccount;
import de.adorsys.ledgers.bank.db.repository.BankAccountRepository;
import de.adorsys.ledgers.postings.api.domain.AccountStmtBO;
import de.adorsys.ledgers.postings.api.domain.LedgerAccountBO;
import de.adorsys.ledgers.postings.api.domain.LedgerBO;
import de.adorsys.ledgers.postings.api.domain.PostingLineBO;
import de.adorsys.ledgers.postings.api.domain.PostingTraceBO;
import de.adorsys.ledgers.postings.api.service.AccountStmtService;
import de.adorsys.ledgers.postings.api.service.LedgerService;
import de.adorsys.ledgers.postings.api.service.PostingService;
import de.adorsys.ledgers.util.Ids;
import de.adorsys.ledgers.util.exception.DepositModuleException;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;//NOPMD
import java.util.stream.Collectors;

import static de.adorsys.ledgers.bank.api.domain.BalanceTypeBO.CLOSING_BOOKED;
import static de.adorsys.ledgers.bank.api.domain.BalanceTypeBO.INTERIM_AVAILABLE;
import static de.adorsys.ledgers.util.exception.DepositErrorCode.*;//NOPMD
import static java.lang.String.format;

@Slf4j
@Service
public class BankAccountServiceImpl extends AbstractServiceImpl implements BankAccountService {
    private static final String MSG_IBAN_NOT_FOUND = "Accounts with iban %s and currency %s not found";
    private static final String MSG_ACCOUNT_NOT_FOUND = "Account with id %s not found";
    private static final String ADDITIONAL_INFO_MESSAGE = "Transaction was posted in Ledgers, debtorName:%s, creditorName:%s, valueDate:%s";

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper = Mappers.getMapper(BankAccountMapper.class);
    private final AccountStmtService accountStmtService;
    private final PostingService postingService;
    private final TransactionDetailsMapper transactionDetailsMapper;
    private final CurrencyExchangeRatesService exchangeRatesService;

    public BankAccountServiceImpl(BankAccountConfigService bankAccountConfigService,
                                     LedgerService ledgerService, BankAccountRepository bankAccountRepository,
                                     AccountStmtService accountStmtService,
                                     PostingService postingService, TransactionDetailsMapper transactionDetailsMapper,
                                     CurrencyExchangeRatesService exchangeRatesService) {
        super(bankAccountConfigService, ledgerService);
        this.bankAccountRepository = bankAccountRepository;
        this.accountStmtService = accountStmtService;
        this.postingService = postingService;
        this.transactionDetailsMapper = transactionDetailsMapper;
        this.exchangeRatesService = exchangeRatesService;
    }

    @Override
    public List<BankAccountBO> getAccountsByIbanAndParamCurrency(String iban, String currency) {
        return bankAccountMapper.toBankAccountListBO(bankAccountRepository.findAllByIbanAndCurrencyContaining(iban, currency));
    }

    @Override
    public BankAccountBO getAccountByIbanAndCurrency(String iban, Currency currency) {
        return getOptionalAccountByIbanAndCurrency(iban, currency)
                       .orElseThrow(() -> DepositModuleException.builder()
                                                  .errorCode(DEPOSIT_ACCOUNT_NOT_FOUND)
                                                  .devMsg(format(MSG_IBAN_NOT_FOUND, iban, currency))
                                                  .build());
    }

    @Override
    public BankAccountBO getAccountById(String accountId) {
        return getOptionalAccountById(accountId)
                       .orElseThrow(() -> DepositModuleException.builder()
                                                  .errorCode(DEPOSIT_ACCOUNT_NOT_FOUND)
                                                  .devMsg(format(MSG_ACCOUNT_NOT_FOUND, accountId))
                                                  .build());
    }

    @Override
    public Optional<BankAccountBO> getOptionalAccountByIbanAndCurrency(String iban, Currency currency) {
        return bankAccountRepository.findByIbanAndCurrency(iban, getCurrencyOrEmpty(currency))
                       .map(bankAccountMapper::toBankAccountBO);
    }

    @Override
    public Optional<BankAccountBO> getOptionalAccountById(String accountId) {
        return bankAccountRepository.findById(accountId)
                       .map(bankAccountMapper::toBankAccountBO);
    }

    @Override
    public BankAccountDetailsBO getAccountDetailsByIbanAndCurrency(String iban, Currency currency, LocalDateTime refTime, boolean withBalances) {
        return getOptionalAccountByIbanAndCurrency(iban, currency)
                       .map(d -> new BankAccountDetailsBO(d, getBalancesList(d, withBalances, refTime)))
                       .orElseThrow(() -> DepositModuleException.builder()
                                                  .errorCode(DEPOSIT_ACCOUNT_NOT_FOUND)
                                                  .devMsg(format(MSG_IBAN_NOT_FOUND, iban, currency))
                                                  .build());
    }

    @Override
    public BankAccountDetailsBO getAccountDetailsById(String accountId, LocalDateTime refTime, boolean withBalances) {
        BankAccountBO bankAccountBO = getBankAccountById(accountId);
        return new BankAccountDetailsBO(bankAccountBO, getBalancesList(bankAccountBO, withBalances, refTime));
    }

    @Override
    public TransactionDetailsBO getTransactionById(String accountId, String transactionId) {
        BankAccountBO account = getBankAccountById(accountId);
        LedgerAccountBO ledgerAccountBO = ledgerService.findLedgerAccountById(account.getLinkedAccounts());
        PostingLineBO line = postingService.findPostingLineById(ledgerAccountBO, transactionId);
        return enrichAdditionalInformation(transactionDetailsMapper.toTransactionSigned(line));
    }

    @Override
    public List<TransactionDetailsBO> getTransactionsByDates(String accountId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        BankAccountBO account = getBankAccountById(accountId);
        LedgerAccountBO ledgerAccountBO = ledgerService.findLedgerAccountById(account.getLinkedAccounts());
        return postingService.findPostingsByDates(ledgerAccountBO, dateFrom, dateTo)
                       .stream()
                       .map(transactionDetailsMapper::toTransactionSigned)
                       .map(this::enrichAdditionalInformation)
                       .collect(Collectors.toList());
    }

    @Override
    public Page<TransactionDetailsBO> getTransactionsByDatesPaged(String accountId, LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable) {
        BankAccountBO account = getBankAccountById(accountId);
        LedgerAccountBO ledgerAccountBO = ledgerService.findLedgerAccountById(account.getLinkedAccounts());

        return postingService.findPostingsByDatesPaged(ledgerAccountBO, dateFrom, dateTo, pageable)
                       .map(transactionDetailsMapper::toTransactionSigned)
                       .map(this::enrichAdditionalInformation);
    }

    @Override
    public boolean confirmationOfFunds(FundsConfirmationRequestBO requestBO) {
        BankAccountDetailsBO account = getAccountDetailsByIbanAndCurrency(requestBO.getPsuAccount().getIban(), requestBO.getPsuAccount().getCurrency(), LocalDateTime.now(), true);
        AmountBO instructedAmount = requestBO.getInstructedAmount();
        BigDecimal requestedAmountInAccountCurrency = exchangeRatesService.applyRate(instructedAmount.getCurrency(), account.getAccount().getCurrency(), instructedAmount.getAmount());
        return account.getBalances().stream()
                       .filter(b -> b.getBalanceType() == INTERIM_AVAILABLE)
                       .findFirst()
                       .map(b -> b.isSufficientAmountAvailable(requestedAmountInAccountCurrency, account.getAccount().getCreditLimit()))
                       .orElse(Boolean.FALSE);
    }

    @Override
    public String readIbanById(String id) {
        return bankAccountRepository.findById(id).map(BankAccount::getIban).orElse(null);
    }

    @Override
    public List<BankAccountDetailsBO> findDetailsByBranch(String branch) {
        List<BankAccount> accounts = bankAccountRepository.findByBranch(branch);
        List<BankAccountBO> accountsBO = bankAccountMapper.toBankAccountListBO(accounts);
        List<BankAccountDetailsBO> accountDetails = new ArrayList<>();
        for (BankAccountBO accountBO : accountsBO) {
            accountDetails.add(new BankAccountDetailsBO(accountBO, Collections.emptyList()));
        }
        return accountDetails;
    }

    @Override
    public Page<BankAccountDetailsBO> findDetailsByBranchPaged(String branch, String queryParam, boolean withBalance, Pageable pageable) {
        return bankAccountRepository.findByBranchAndIbanContaining(branch, queryParam, pageable)
                       .map(bankAccountMapper::toBankAccountBO)
                       .map(d -> new BankAccountDetailsBO(d, getBalancesList(d, withBalance, LocalDateTime.now())));
    }


    @Override
    @Transactional
    public void changeAccountsBlockedStatus(String userId, boolean isSystemBlock, boolean lockStatusToSet) {
        if (isSystemBlock) {
            bankAccountRepository.updateSystemBlockedStatus(userId, lockStatusToSet);
        } else {
            bankAccountRepository.updateBlockedStatus(userId, lockStatusToSet);
        }
    }

    @Override
    public Page<BankAccountBO> findByBranchIdsAndMultipleParams(Collection<String> branchIds, String iban, Boolean blocked, Pageable pageable) {
        List<Boolean> blockedQueryParam = Optional.ofNullable(blocked)
                                                  .map(Arrays::asList)
                                                  .orElseGet(() -> Arrays.asList(true, false));
        return bankAccountRepository.findByBranchInAndIbanContainingAndBlockedInAndSystemBlockedFalse(branchIds, iban, blockedQueryParam, pageable)
                       .map(bankAccountMapper::toBankAccountBO);
    }

    @Override
    public void changeAccountsBlockedStatus(Set<String> accountIds, boolean isSystemBlock, boolean lockStatusToSet) {
        if (isSystemBlock) {
            bankAccountRepository.updateSystemBlockedStatus(accountIds, lockStatusToSet);
        } else {
            bankAccountRepository.updateBlockedStatus(accountIds, lockStatusToSet);
        }
    }

    @Override
    @Transactional
    public void changeCreditLimit(String accountId, BigDecimal creditLimit) {
        BankAccount account = getBankAccountEntityById(accountId);
        checkCreditLimitIsCorrect(creditLimit);
        account.setCreditLimit(creditLimit);
    }

    @Override
    public BankAccountBO createNewAccount(BankAccountBO bankAccountBO, String userName, String branch) {
        checkBankAccountAlreadyExist(bankAccountBO);
        checkCreditLimitIsCorrect(bankAccountBO.getCreditLimit());
        BankAccount bankAccount = bankAccountMapper.toBankAccount(bankAccountBO);
        bankAccount.setId(Ids.id());
        bankAccount.setName(userName);

        LedgerAccountBO parentLedgerAccount = new LedgerAccountBO(bankAccountConfigService.getBankParentAccount(), loadLedger());
        LedgerAccountBO ledgerAccount = new LedgerAccountBO(bankAccount.getIban(), parentLedgerAccount);

        String accountId = ledgerService.newLedgerAccount(ledgerAccount, userName).getId();
        bankAccount.setLinkedAccounts(accountId);

        Optional.ofNullable(branch).ifPresent(bankAccount::setBranch);
        BankAccount saved = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.toBankAccountBO(saved);
    }

    private TransactionDetailsBO enrichAdditionalInformation(TransactionDetailsBO transactionDetailsBO) {
        String additionalInfo = format(ADDITIONAL_INFO_MESSAGE, transactionDetailsBO.getDebtorName(), transactionDetailsBO.getCreditorName(), transactionDetailsBO.getValueDate());
        transactionDetailsBO.setAdditionalInformation(additionalInfo);
        return transactionDetailsBO;
    }

    private void checkCreditLimitIsCorrect(BigDecimal creditLimit) {
        if (creditLimit.signum() < 0) {
            throw DepositModuleException.builder()
                          .errorCode(UNSUPPORTED_CREDIT_LIMIT)
                          .devMsg("Credit limit value should be positive or zero")
                          .build();
        }
    }

    private void checkBankAccountAlreadyExist(BankAccountBO bankAccountBO) {
        boolean isExistingAccount = bankAccountRepository.findByIbanAndCurrency(bankAccountBO.getIban(), getCurrencyOrEmpty(bankAccountBO.getCurrency()))
                                            .isPresent();
        if (isExistingAccount) {
            String message = format("Deposit account already exists. IBAN %s. Currency %s",
                                    bankAccountBO.getIban(), bankAccountBO.getCurrency().getCurrencyCode());
            throw DepositModuleException.builder()
                          .errorCode(DEPOSIT_ACCOUNT_EXISTS)
                          .devMsg(message)
                          .build();
        }
    }

    private String getCurrencyOrEmpty(Currency currency) {
        return Optional.ofNullable(currency)
                       .map(Currency::getCurrencyCode)
                       .orElse("");
    }

    private List<BalanceBO> getBalancesList(BankAccountBO d, boolean withBalances, LocalDateTime refTime) {
        return withBalances
                       ? getBalances(d.getLinkedAccounts(), d.getCurrency(), refTime)
                       : Collections.emptyList();
    }

    private BankAccountBO getBankAccountById(String accountId) {
        return bankAccountMapper.toBankAccountBO(getBankAccountEntityById(accountId));
    }

    private BankAccount getBankAccountEntityById(String accountId) {
        return bankAccountRepository.findById(accountId)
                       .orElseThrow(() -> DepositModuleException.builder()
                                                  .errorCode(DEPOSIT_ACCOUNT_NOT_FOUND)
                                                  .devMsg(format("Account with id: %s not found!", accountId))
                                                  .build());
    }

    private List<BalanceBO> getBalances(String id, Currency currency, LocalDateTime refTime) {
        LedgerBO ledger = loadLedger();
        LedgerAccountBO ledgerAccountBO = new LedgerAccountBO();
        ledgerAccountBO.setLedger(ledger);
        ledgerAccountBO.setId(id);
        return getBalances(currency, refTime, ledgerAccountBO);
    }

    private List<BalanceBO> getBalances(Currency currency, LocalDateTime refTime, LedgerAccountBO ledgerAccountBO) {
        AccountStmtBO stmt = accountStmtService.readStmt(ledgerAccountBO, refTime);
        BalanceBO interimBalance = composeBalance(currency, stmt, INTERIM_AVAILABLE);
        BalanceBO closingBalance = composeBalance(currency, stmt, CLOSING_BOOKED);
        return Arrays.asList(interimBalance, closingBalance);
    }

    private BalanceBO composeBalance(Currency currency, AccountStmtBO stmt, BalanceTypeBO balanceType) {
        BalanceBO balanceBO = new BalanceBO();
        AmountBO amount = new AmountBO(currency, stmt.creditBalance());
        balanceBO.setAmount(amount);
        balanceBO.setBalanceType(balanceType);
        balanceBO.setReferenceDate(stmt.getPstTime().toLocalDate());
        return composeFinalBalance(balanceBO, stmt);
    }

    private BalanceBO composeFinalBalance(BalanceBO balance, AccountStmtBO stmt) {
        PostingTraceBO youngestPst = stmt.getYoungestPst();
        if (youngestPst != null) {
            balance.setLastChangeDateTime(youngestPst.getSrcPstTime());
            balance.setLastCommittedTransaction(youngestPst.getSrcPstId());
        } else {
            balance.setLastChangeDateTime(stmt.getPstTime());
        }
        return balance;
    }
}
