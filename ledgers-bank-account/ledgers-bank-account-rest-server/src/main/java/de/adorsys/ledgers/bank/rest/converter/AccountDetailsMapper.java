/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.bank.rest.converter;

import de.adorsys.ledgers.bank.api.domain.BalanceBO;
import de.adorsys.ledgers.bank.api.domain.BankAccountBO;
import de.adorsys.ledgers.bank.api.domain.BankAccountDetailsBO;
import de.adorsys.ledgers.bank.api.domain.account.AccountDetailsExtendedTO;
import de.adorsys.ledgers.bank.api.domain.account.AccountDetailsTO;
import de.adorsys.ledgers.bank.api.domain.account.AccountStatusTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AccountDetailsMapper {

    @Mapping(target = "accountStatus", expression = "java(toAccountStatusTO(details))")
    public abstract AccountDetailsTO toAccountDetailsTO(BankAccountBO details, List<BalanceBO> balances);

    public abstract AccountDetailsExtendedTO toAccountDetailsExtendedTO(BankAccountBO source, String branchLogin);

    public abstract BankAccountBO toBankAccountBO(AccountDetailsTO source);

    public AccountDetailsTO toAccountDetailsTO(BankAccountDetailsBO source) {
        return toAccountDetailsTO(source.getAccount(), source.getBalances());
    }

    public abstract List<AccountDetailsTO> toAccountDetailsTOList(List<BankAccountDetailsBO> source);

    public abstract List<AccountDetailsTO> toAccountDetailsList(List<BankAccountBO> source);

    protected AccountStatusTO toAccountStatusTO(BankAccountBO details) {
        return details.isEnabled()
                ? AccountStatusTO.ENABLED
                : AccountStatusTO.BLOCKED;
    }
}
