package de.adorsys.webank.bank.api.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import de.adorsys.webank.bank.api.domain.BankAccountBO;
import de.adorsys.webank.bank.api.service.BankAccountService;
import de.adorsys.webank.bank.api.service.util.BankAccountCertificateCreationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Arrays.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.security.NoSuchAlgorithmException;

@Service
public class BankAccountCertificateCreationServiceImpl implements BankAccountCertificateCreationService {

    private static final Logger log = LoggerFactory.getLogger(BankAccountCertificateCreationServiceImpl.class);
    private static  final long EXPIRATION_DAYS = 30;
    private final BankAccountService bankAccountService;

    @Value("${server.private.key}")
    private String serverPrivateKeyJson;

    @Value("${server.public.key}")
    private String serverPublicKeyJson;

    @Autowired
    public BankAccountCertificateCreationServiceImpl(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Override
    public String registerNewBankAccount(String phoneNumber, String devicePublicKey, BankAccountBO bankAccountBO, String userName, String branch) {
        // Create the new account
        BankAccountBO createdAccount = bankAccountService.createNewAccount(bankAccountBO, userName, branch);
        boolean isAccountCreated = createdAccount != null && createdAccount.getId() != null;
        if (!isAccountCreated) {
            throw new IllegalStateException("Failed to create bank account.");
        }
        // Generate the certificate using the created account's ID
        return "\nAccount ID:\n" +createdAccount.getId() + "\nAccount certificate:\n" +  generateBankAccountCertificate(devicePublicKey, createdAccount.getId());
    }

    @Override
    public String generateBankAccountCertificate(String devicePublicKey, String accountId) {
        try {

            // Parse the server's private key from the JWK JSON string
            ECKey serverPrivateKey = (ECKey) JWK.parse(serverPrivateKeyJson);

            // Check that the private key contains the 'd' (private) parameter for signing
            if (serverPrivateKey.getD() == null) {
                throw new IllegalStateException("Private key 'd' (private) parameter is missing.");
            }

            JWSSigner signer = new ECDSASigner(serverPrivateKey);

            // Compute hash of the device's public key
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedDevicePubKey = digest.digest(devicePublicKey.getBytes(StandardCharsets.UTF_8));
            String devicePubKeyHash = Base64.getEncoder().encodeToString(hashedDevicePubKey);

            // Compute hash of the account ID
            byte[] hashedAccountId = digest.digest(accountId.getBytes(StandardCharsets.UTF_8));
            String accountIdHash = HashUtil.hashToHex(hashedAccountId);

            // Calculate expiration timestamp
            long expirationTime = Instant.now().plusSeconds(EXPIRATION_DAYS * 86400).getEpochSecond();
            // Create JWT payload including phoneHash, devicePubKeyHash, and accountIdHash
            String payloadData = String.format("{\"acc\": \"%s\", \"exp\": %d, \"cnf\": \"%s\"}", accountIdHash, expirationTime, devicePubKeyHash);
            Payload payload = new Payload(payloadData);

            // Parse the server's public key from the JWK JSON string
            ECKey serverPublicKey = (ECKey) JWK.parse(serverPublicKeyJson);

            // Create the JWT header with the JWK object (the server public key)
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                    .type(JOSEObjectType.JWT)
                    .jwk(serverPublicKey.toPublicJWK()) // Add JWK to the header
                    .build();

            // Build the JWS object
            JWSObject jwsObject = new JWSObject(header, payload);
            jwsObject.sign(signer);

            return jwsObject.serialize();
        } catch (Exception e) {
            // Log the exception for debugging
            log.error("Error generating device certificate", e);
            throw new IllegalStateException("Error generating device certificate");
        }
    }

    public static class HashUtil {
        public static String hashToHex(byte[] hashedBytes) {
            // Convert to Hex encoding
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        }
    }
}