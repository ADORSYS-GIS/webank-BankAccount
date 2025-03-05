package de.adorsys.webank.bank.api.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import de.adorsys.webank.bank.api.domain.BankAccountBO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.Instant;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountCertificateCreationServiceImplTest {

    @Mock
    private BankAccountServiceImpl bankAccountService;

    @InjectMocks
    private BankAccountCertificateCreationServiceImpl service;

    private ECKey ecKey;

    @BeforeEach
    void setUp() throws Exception {
        // Generate a new EC key pair for testing
        ecKey = new ECKeyGenerator(Curve.P_256)
                .keyID("123")
                .generate();

        // Inject the server's private and public keys into the service
        ReflectionTestUtils.setField(service, "serverPrivateKeyJson", ecKey.toJSONString());
        ReflectionTestUtils.setField(service, "serverPublicKeyJson", ecKey.toPublicJWK().toJSONString());
    }

    @Test
    void registerNewBankAccount_Success_ReturnsAccountIdAndCertificate() {
        // Arrange
        BankAccountBO mockAccount = new BankAccountBO();
        mockAccount.setId("account123");
        when(bankAccountService.createNewAccount(any(), any(), any())).thenReturn(mockAccount);

        // Act
        String result = service.registerNewBankAccount("+123456789", "devicePublicKey", new BankAccountBO(), "user", "branch");

        // Assert
        assertTrue(result.contains("Account ID:\naccount123"));
        assertTrue(result.contains("Account certificate:\n"));
    }

    @Test
    void registerNewBankAccount_CreationFails_ThrowsException() {
        // Arrange
        when(bankAccountService.createNewAccount(any(), any(), any())).thenReturn(null);

        // Act & Assert
        BankAccountBO accountBO = new BankAccountBO();
        assertThrows(IllegalStateException.class, () ->
                service.registerNewBankAccount(
                        "+123456789",
                        "devicePublicKey",
                        accountBO, // Created outside lambda
                        "user",
                        "branch"
                )
        );
    }

    @Test
    void generateBankAccountCertificate_ValidInputs_ReturnsValidJWT() throws JOSEException, ParseException, NoSuchAlgorithmException {
        // Arrange
        String devicePublicKey = "testDevicePublicKey";
        String accountId = "testAccount123";

        // Act
        String jwt = service.generateBankAccountCertificate(devicePublicKey, accountId);
        SignedJWT signedJWT = SignedJWT.parse(jwt);


        // Compute expected hashes
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String expectedPhoneHash = hashAndEncode(phone, digest);
        String expectedDeviceHash = hashAndEncode(deviceKey, digest);
        String expectedAccountHash = HashUtil.hashToHex(digest.digest(accountId.getBytes(StandardCharsets.UTF_8)));
        // Verify payload content
        assertEquals(expectedPhoneHash, JSONObjectUtils.getString(JSONObjectUtils.parse(payload), "phoneHash"));
        assertEquals(expectedDeviceHash, JSONObjectUtils.getString(JSONObjectUtils.parse(payload), "devicePubKeyHash"));
        assertEquals(expectedAccountHash, JSONObjectUtils.getString(JSONObjectUtils.parse(payload), "accountIdHash"));
    }

        // Verify the signature
        ECDSAVerifier verifier = new ECDSAVerifier(ecKey.toPublicJWK());
        assertTrue(signedJWT.verify(verifier));

        // Extract claims
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

        // Validate account ID hash
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedAccountId = digest.digest(accountId.getBytes(StandardCharsets.UTF_8));
        String expectedAccHash = Base64.getEncoder().encodeToString(hashedAccountId);
        assertEquals(expectedAccHash, claims.getStringClaim("acc"));

        // Validate device public key hash
        byte[] hashedDeviceKey = digest.digest(devicePublicKey.getBytes(StandardCharsets.UTF_8));
        String expectedCnfHash = Base64.getEncoder().encodeToString(hashedDeviceKey);
        assertEquals(expectedCnfHash, claims.getStringClaim("cnf"));

        // Validate expiration time (within 2 seconds of expected)
        long expectedExp = Instant.now().plusSeconds(30 * 86400).getEpochSecond();
        long actualExp = claims.getExpirationTime().toInstant().getEpochSecond();
        assertTrue(Math.abs(expectedExp - actualExp) <= 2);

        // Validate header
        assertEquals(JWSAlgorithm.ES256, signedJWT.getHeader().getAlgorithm());
        assertEquals(JOSEObjectType.JWT, signedJWT.getHeader().getType());
        assertNotNull(signedJWT.getHeader().getJWK());
    }

    @Test
    void generateBankAccountCertificate_InvalidPrivateKey_ThrowsException() {
        // Arrange: Use a public key (without 'd') as the private key
        ECKey publicKey = ecKey.toPublicJWK();
        ReflectionTestUtils.setField(service, "serverPrivateKeyJson", publicKey.toJSONString());

    private String hashAndEncode(String input, MessageDigest digest) {
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        digest.reset();
        return Base64.getEncoder().encodeToString(hash);
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

    private static class TestConstants {
        static final String SERVER_PUBLIC_KEY_JSON = "{ \"kty\": \"EC\", \"crv\": \"P-256\", " +
                "\"x\": \"PHlAcVDiqi7130xWiMn5CEbOyg_Yo0qfOhabhPlDV_s\", " +
                "\"y\": \"N5bqvbDjbsX2uo2_lzKrwPt7fySMweZVeFSAv99TEEc\" }";
        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
                service.generateBankAccountCertificate("deviceKey", "account123"));
    }
}