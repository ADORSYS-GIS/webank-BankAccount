/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.db.repository;

import de.adorsys.webank.bank.db.domain.BankAccount;
import de.adorsys.webank.bank.db.test.BankAccountRepositoryApplication;
import de.adorsys.ledgers.util.Ids;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BankAccountRepositoryApplication.class)
class BankAccountRepositoryIT {

    @Autowired
    private BankAccountRepository repo;

    @Test
    void create_da_mandatory_properties() {
        BankAccount da = new BankAccount();
        da.setId(Ids.id());
        da.setCurrency("EUR");
        da.setIban("DE89370400440532013000");
        BankAccount account = repo.save(da);
        assertNotNull(account);
    }

}
