/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.service.impl.mockbank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.adorsys.webank.bank.api.service.BankAccountConfigService;
import de.adorsys.webank.bank.api.service.BankAccountInitService;
import de.adorsys.webank.bank.api.service.impl.test.BankAccountServiceApplication;
import de.adorsys.ledgers.postings.api.domain.LedgerAccountBO;
import de.adorsys.ledgers.postings.api.domain.LedgerBO;
import de.adorsys.ledgers.postings.api.domain.PostingBO;
import de.adorsys.ledgers.postings.api.service.AccountStmtService;
import de.adorsys.ledgers.postings.api.service.LedgerService;
import de.adorsys.ledgers.postings.api.service.PostingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BankAccountServiceApplication.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@Slf4j
class BankAccountServiceImplIT {

    @Autowired
    private AccountStmtService accountStmtService;
    @Autowired
    private PostingService postingService;
    @Autowired
    private LedgerService ledgerService;
    @Autowired
    private BankAccountConfigService bankAccountConfigService;
    @Autowired
    private BankAccountInitService bankAccountInitService;

    private ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    @BeforeEach
    void initBankAccount() {
        bankAccountInitService.initConfigData();
    }

    /**
     * Testing the test. Negative case, if comparison with wrong balance works.
     */
    @Test
    void use_case_newbank_no_overriden_tx_nok() throws IOException {
        loadPosting("use_case_newbank_no_overriden_tx.yml");

        LocalDateTime dateTime = LocalDateTime.of(2018, Month.JANUARY, 1, 23, 59);
        checkBalance("1104", dateTime, new BigDecimal("4200000.00"));
        checkBalance("3001", dateTime, BigDecimal.valueOf(-4200000.00));
        checkWrongBalance("1104", dateTime, new BigDecimal("0.00"));
    }

    /**
     * Classical case, no overridden transaction. Test balance computation.
     */
    @Test
    void use_case_newbank_no_overriden_tx_ok() throws IOException {
        loadPosting("use_case_newbank_no_overriden_tx.yml");

        LocalDateTime dateTime = LocalDateTime.of(2018, Month.JANUARY, 1, 8, 30);
        checkBalance("1104", dateTime, new BigDecimal("0.00"));
        checkBalance("3001", dateTime, new BigDecimal("0.00"));
        dateTime = LocalDateTime.of(2018, Month.JANUARY, 1, 23, 59);
        checkBalance("1104", dateTime, new BigDecimal("4200000.00"));
        checkBalance("3001", dateTime, BigDecimal.valueOf(-4200000.00));

        dateTime = LocalDateTime.of(2018, Month.JANUARY, 2, 23, 59);
        checkBalance("11240", dateTime, new BigDecimal("2000000.00"));
        checkBalance("1104", dateTime, new BigDecimal("2200000.00"));

        dateTime = LocalDateTime.of(2018, Month.JANUARY, 3, 23, 59);
        checkBalance("11240", dateTime, new BigDecimal("1835600.00"));
        checkBalance("1810", dateTime, new BigDecimal("14400.00"));
        checkBalance("1001", dateTime, new BigDecimal("150000.00"));

        dateTime = LocalDateTime.of(2018, Month.JANUARY, 8, 23, 59);
        checkBalance("11240", dateTime, new BigDecimal("1803600.00"));
        checkBalance("5057", dateTime, new BigDecimal("32000.00"));

        dateTime = LocalDateTime.of(2018, Month.JANUARY, 12, 23, 59);
        checkBalance("1001", dateTime, new BigDecimal("156500.00"));
        checkBalance("DE69760700240340283600", dateTime, BigDecimal.valueOf(-1500.00));
        checkBalance("1006", dateTime, new BigDecimal("3500.00"));
        checkBalance("DE80760700240271232400", dateTime, BigDecimal.valueOf(-3500.00));
        checkBalance("DE38760700240320465700", dateTime, BigDecimal.valueOf(-5000.00));
    }

    @Test
    void use_case_check_account_balance() throws IOException {
        loadPosting("use_case_newbank_no_overriden_tx.yml");
        checkBalance("DE38760700240320465700", LocalDateTime.now(), new BigDecimal(-5000.00));
    }

    private void checkBalance(String accountNumber, LocalDateTime date, BigDecimal expectedBalance) {
        LedgerBO ledger = loadLedger();
        LedgerAccountBO account = loadLedgerAccount(ledger, accountNumber);
        BigDecimal balance = accountStmtService.readStmt(account, date).debitBalance();
        assertEquals(expectedBalance.doubleValue(), balance.doubleValue(), 0d);
    }

    private void checkWrongBalance(String accountNumber, LocalDateTime date, BigDecimal expectedBalance) {
        LedgerBO ledger = loadLedger();
        LedgerAccountBO account = loadLedgerAccount(ledger, accountNumber);
        BigDecimal balance = accountStmtService.readStmt(account, date).debitBalance();
        assertNotEquals(expectedBalance.doubleValue(), balance.doubleValue(), 0d);
    }

    private void loadPosting(String s) throws IOException {
        LedgerBO ledger = loadLedger();
        assumeTrue(ledger != null);
        InputStream inputStream = BankAccountServiceImplIT.class.getResourceAsStream(s);
        PostingBO[] postings = mapper.readValue(inputStream, PostingBO[].class);
        for (PostingBO p : postings) {
            p.setLedger(ledger);
            p.getLines().forEach(pl -> {
                pl.getAccount().setLedger(ledger);
            });
            try {
                postingService.newPosting(p);
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error("Error adding posting in test");
            }
        }
    }

    private LedgerBO loadLedger() {
        String ledgerName = bankAccountConfigService.getLedger();
        return ledgerService.findLedgerByName(ledgerName).orElseThrow(() -> new IllegalStateException(String.format("Ledger with name %s not found", ledgerName)));
    }

    public LedgerAccountBO loadLedgerAccount(LedgerBO ledger, String accountNumber) {
        return ledgerService.findLedgerAccount(ledger, accountNumber);
    }

}
