package de.adorsys.webank.bank.api.service.util;

import de.adorsys.webank.bank.api.domain.BankAccountBO;

public interface BankAccountCertificateCreationService {

    String registerNewBankAccount(String phoneNumber, String devicePublicKey, BankAccountBO bankAccountBO, String userName, String branch);

    String generateBankAccountCertificate(String phoneNumber, String devicePublicKey, String accountId);
}
