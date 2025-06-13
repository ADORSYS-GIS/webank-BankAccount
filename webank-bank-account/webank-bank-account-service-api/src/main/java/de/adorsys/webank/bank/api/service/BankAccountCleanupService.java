/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.service;

import java.time.LocalDateTime;

public interface BankAccountCleanupService {

    void deleteTransactions(String accountId);

    void deleteBranch(String branchId);

    void deleteUser(String userId);

    void deleteAccount(String accountId);

    void rollBackBranch(String branch, LocalDateTime revertTimestamp);
}
