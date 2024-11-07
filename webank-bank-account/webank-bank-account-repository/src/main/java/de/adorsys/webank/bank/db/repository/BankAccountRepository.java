/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.db.repository;

import de.adorsys.webank.bank.db.domain.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BankAccountRepository extends PagingAndSortingRepository<BankAccount, String>, CrudRepository<BankAccount, String> {
    List<BankAccount> findByBranch(String branch);

    Page<BankAccount> findByBranchAndIbanContaining(String branch, String queryParam, Pageable pageable);

    Page<BankAccount> findByBranchInAndIbanContainingAndBlockedInAndSystemBlockedFalse(Collection<String> branchIds, String iban, List<Boolean> blocked, Pageable pageable);

    Optional<BankAccount> findByIbanAndCurrency(String iban, String currency);

    List<BankAccount> findAllByIbanAndCurrencyContaining(String iban, String currency);

    @Query("update BankAccount a set a.systemBlocked=?2 where a.branch=?1")
    void updateSystemBlockedStatus(String userId, boolean lockStatusToSet);

    @Modifying
    @Query("update BankAccount a set a.blocked=?2 where a.branch=?1")
    void updateBlockedStatus(String userId, boolean lockStatusToSet);

    @Query("update BankAccount da set da.systemBlocked=?2 where da.id in ?1")
    void updateSystemBlockedStatus(Set<String> accountIds, boolean lockStatusToSet);

    @Modifying
    @Query("update BankAccount da set da.blocked=?2 where da.id in ?1")
    void updateBlockedStatus(Set<String> accountIds, boolean lockStatusToSet);
}
