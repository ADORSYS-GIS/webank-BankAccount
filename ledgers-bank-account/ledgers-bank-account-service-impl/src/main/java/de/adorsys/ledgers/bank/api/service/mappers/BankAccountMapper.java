/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.bank.api.service.mappers;

import de.adorsys.ledgers.bank.api.domain.AccountReferenceBO;
import de.adorsys.ledgers.bank.api.domain.BankAccountBO;
import de.adorsys.ledgers.bank.db.domain.AccountReference;
import de.adorsys.ledgers.bank.db.domain.BankAccount;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    BankAccountBO toBankAccountBO(BankAccount BankAccount);

    List<BankAccountBO> toBankAccountListBO(List<BankAccount> list);

    BankAccount toBankAccount(BankAccountBO BankAccount);

    AccountReferenceBO toAccountReferenceBO(BankAccount BankAccount);

    AccountReference toAccountReference(AccountReferenceBO reference);

}
