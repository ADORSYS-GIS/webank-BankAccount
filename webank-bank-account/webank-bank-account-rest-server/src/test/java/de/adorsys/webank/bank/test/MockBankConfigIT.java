/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */
package de.adorsys.webank.bank.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import de.adorsys.webank.bank.test.loaded.LoadedLedgersApplication;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LoadedLedgersApplication.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ActiveProfiles("h2")
public class MockBankConfigIT {
    @Test
    void should_run_init_config_data_without_exception() {
        assertTrue(true);
    }
}
