/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.bank.rest.resource;

import de.adorsys.ledgers.bank.api.domain.account.AccountDetailsTO;
import de.adorsys.ledgers.bank.api.resource.AccountMgmResourceAPI;
import de.adorsys.ledgers.bank.api.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 @RestController
 @RequiredArgsConstructor
 @Service
 @RequestMapping("/AccountManagement" )
 public class AccountMgmResource implements AccountMgmResourceAPI {
     private final BankAccountService bankAccountService;

     @Override
     public ResponseEntity<AccountDetailsTO> createBankAccount(String authorizationHeader, AccountDetailsTO accountDetailsTO) {
         return null;
     }
 }