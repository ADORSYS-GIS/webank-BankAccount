/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.service.mappers;

import de.adorsys.webank.bank.api.domain.AccountTypeBO;
import de.adorsys.webank.bank.api.domain.AccountUsageBO;
import de.adorsys.webank.bank.api.domain.BankAccountBO;
import de.adorsys.webank.bank.db.domain.AccountType;
import de.adorsys.webank.bank.db.domain.AccountUsage;
import de.adorsys.webank.bank.db.domain.BankAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BankAccountMapperTest {

    private static final LocalDateTime CREATED = LocalDateTime.now();

    @InjectMocks
    private BankAccountMapperImpl bankAccountMapper;

    @Test
    void toBankAccountBO() {
        // When
        BankAccountBO account = bankAccountMapper.toBankAccountBO(getBankAccount());

        //Then
        assertEquals(account, getBankAccountBO());
    }

    @Test
    void toBankAccount() {
        // When
        BankAccount account = bankAccountMapper.toBankAccount(getBankAccountBO());

        //Then
        assertEquals(account, getBankAccount());
    }

    private BankAccount getBankAccount() {
        return new BankAccount("id", "iban", "msisdn", "EUR",
                                  "name","displayName", "product", null, AccountType.CASH, "bic",
                                  "Some linked account", AccountUsage.PRIV, "details", false, false, CREATED, BigDecimal.ZERO);
    }

    private BankAccountBO getBankAccountBO() {
        BankAccountBO bo = new BankAccountBO();
        bo.setId("id");
        bo.setIban("iban");
        bo.setMsisdn("msisdn");
        bo.setCurrency(Currency.getInstance("EUR"));
        bo.setName("name");
        bo.setProduct("product");
        bo.setAccountType(AccountTypeBO.CASH);
        bo.setBic("bic");
        bo.setDisplayName("displayName");
        bo.setLinkedAccounts("Some linked account");
        bo.setUsageType(AccountUsageBO.PRIV);
        bo.setDetails("details");
        bo.setCreated(CREATED);
        bo.setCreditLimit(BigDecimal.ZERO);
        return bo;
    }
}
