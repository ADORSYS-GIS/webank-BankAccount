/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.rest.resource;

import de.adorsys.webank.bank.api.domain.BankAccountBO;
import de.adorsys.webank.bank.api.domain.BankAccountDetailsBO;
import de.adorsys.webank.bank.api.domain.account.AccountDetailsTO;
import de.adorsys.webank.bank.api.resource.AccountMgmResourceAPI;
import de.adorsys.webank.bank.api.service.BankAccountService;
import de.adorsys.webank.bank.rest.converter.AccountDetailsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
 @RequiredArgsConstructor
 @Service
 @RequestMapping("/AccountManagement" )
 public class AccountMgmResource implements AccountMgmResourceAPI {
     private final BankAccountService bankAccountService;
     private final AccountDetailsMapper accountDetailsMapper;

     @Override
     public ResponseEntity<AccountDetailsTO> createBankAccount(String authorizationHeader, AccountDetailsTO accountDetailsTO) {
         BankAccountBO bankAccountBO = accountDetailsMapper.toBankAccountBO(accountDetailsTO);
         BankAccountBO createdAccount = bankAccountService.createNewAccount(bankAccountBO, accountDetailsTO.getName(), accountDetailsTO.getBranch());
         BankAccountDetailsBO accountDetailsById = bankAccountService.getAccountDetailsById(createdAccount.getId(), LocalDateTime.now(), true);
         AccountDetailsTO accountDetailsTO1 = accountDetailsMapper.toAccountDetailsTO(accountDetailsById);
         return ResponseEntity.ok(accountDetailsTO1);
     }
 }