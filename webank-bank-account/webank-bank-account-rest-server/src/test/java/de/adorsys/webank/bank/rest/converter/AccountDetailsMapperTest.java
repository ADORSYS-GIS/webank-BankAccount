/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.rest.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import de.adorsys.webank.bank.api.domain.BalanceBO;
import de.adorsys.webank.bank.api.domain.BankAccountBO;
import de.adorsys.webank.bank.api.domain.account.AccountDetailsTO;
import pro.javatar.commons.reader.YamlReader;

class AccountDetailsMapperTest {
    private AccountDetailsMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(AccountDetailsMapper.class);
    }

    @Test
    void toAccountDetailsTO() throws IOException {
        // Given
        AccountDetailsTO expected = getAccount(AccountDetailsTO.class);

        // When
        AccountDetailsTO details = mapper.toAccountDetailsTO(getAccount(BankAccountBO.class), getBalances(BalanceBO.class));

        // Then
        assertThat(details).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    void toBankAccountBO() throws IOException {
        // When
        BankAccountBO details = mapper.toBankAccountBO(getAccount(AccountDetailsTO.class));

        // Then
        assertThat(details).isEqualToComparingFieldByFieldRecursively(getAccount(BankAccountBO.class));
    }

    private static <T> T getAccount(Class<T> aClass) throws IOException {
        return aClass.equals(AccountDetailsTO.class)
                       ? YamlReader.getInstance().getObjectFromResource(AccountDetailsMapper.class, "AccountDetailsTO.yml", aClass)
                       : YamlReader.getInstance().getObjectFromResource(AccountDetailsMapper.class, "AccountDetails.yml", aClass);
    }

    private static <T> List<T> getBalances(Class<T> tClass) throws IOException {
        return Arrays.asList(
                YamlReader.getInstance().getObjectFromResource(AccountDetailsMapper.class, "Balance1.yml", tClass),
                YamlReader.getInstance().getObjectFromResource(AccountDetailsMapper.class, "Balance2.yml", tClass)
        );
    }
}