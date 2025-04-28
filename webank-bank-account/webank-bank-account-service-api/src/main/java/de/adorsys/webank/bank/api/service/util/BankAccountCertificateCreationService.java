package de.adorsys.webank.bank.api.service.util;

import de.adorsys.webank.bank.api.domain.BankAccountBO;

public interface BankAccountCertificateCreationService {
    String registerNewBankAccount(String devicePublicKey, BankAccountBO bankAccountBO, String userName, String branch);
    String generateBankAccountCertificate(String devicePublicKey, String accountId);
}
