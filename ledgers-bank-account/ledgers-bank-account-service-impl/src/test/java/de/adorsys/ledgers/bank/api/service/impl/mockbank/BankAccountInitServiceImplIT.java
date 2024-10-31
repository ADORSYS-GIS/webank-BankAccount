/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.bank.api.service.impl.mockbank;

import de.adorsys.ledgers.bank.api.service.BankAccountInitService;
import de.adorsys.ledgers.bank.api.service.impl.test.BankAccountServiceApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BankAccountServiceApplication.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
class BankAccountInitServiceImplIT {

    @Autowired
    private BankAccountInitService BankAccountInitService;

    @Test()
    void should_run_init_config_data_without_exception() {
        BankAccountInitService.initConfigData();
        assertTrue(true);
    }
}
