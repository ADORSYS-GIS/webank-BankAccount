package de.adorsys.webank.bank.api.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.util.JSONObjectUtils;
import de.adorsys.webank.bank.api.domain.BankAccountBO;
import de.adorsys.webank.bank.api.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BankAccountCertificateCreationServiceImplTest {

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private BankAccountCertificateCreationServiceImpl certificateCreationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize the server keys for testing
        certificateCreationService.serverPrivateKeyJson = "{ \"kty\": \"EC\", \"crv\": \"P-256\", \"d\": \"E-_KxQl0ow6_4Munq81OH_lg64R2vDpe3zq9XnI0AjE\", \"x\": \"PHlAcVDiqi7130xWiMn5CEbOyg_Yo0qfOhabhPlDV_s\", \"y\": \"N5bqvbDjbsX2uo2_lzKrwPt7fySMweZVeFSAv99TEEc\" }";
        certificateCreationService.serverPublicKeyJson = "{ \"kty\": \"EC\", \"crv\": \"P-256\", \"x\": \"PHlAcVDiqi7130xWiMn5CEbOyg_Yo0qfOhabhPlDV_s\", \"y\": \"N5bqvbDjbsX2uo2_lzKrwPt7fySMweZVeFSAv99TEEc\" }";
    }

    @Test
    void testRegisterNewBankAccount_Success() {
        BankAccountBO account = new BankAccountBO();
        account.setId("123");
        when(bankAccountService.createNewAccount(any(), any(), any())).thenReturn(account);

        String result = certificateCreationService.registerNewBankAccount(
                "1234567890", "deviceKey", new BankAccountBO(), "user", "branch");

        assertTrue(result.contains("Account ID:\n123"));
        assertTrue(result.contains("Account certificate:"));
        verify(bankAccountService, times(1)).createNewAccount(any(), any(), any());
    }

    @Test
    void testRegisterNewBankAccount_Failure() {
        when(bankAccountService.createNewAccount(any(), any(), any())).thenReturn(null);

        assertThrows(IllegalStateException.class, () ->
                certificateCreationService.registerNewBankAccount(
                        "123", "key", new BankAccountBO(), "user", "branch"));
    }

    @Test
    void testGenerateBankAccountCertificate_Success() throws Exception {
        String certificate = certificateCreationService.generateBankAccountCertificate(
                "1234567890", "deviceKey", "account123");

        JWSObject jws = JWSObject.parse(certificate);
        assertNotNull(jws.getHeader());
        assertNotNull(jws.getPayload());
        assertNotNull(jws.getSignature());

        // Verify signature
        ECKey publicKey = (ECKey) JWK.parse(certificateCreationService.serverPublicKeyJson);
        assertTrue(jws.verify(new ECDSAVerifier(publicKey.toECPublicKey())));
    }

    @Test
    void testGenerateBankAccountCertificate_HashCorrectness() throws Exception {
        String phone = "1234567890";
        String deviceKey = "publicKey123";
        String accountId = "account456";

        String certificate = certificateCreationService.generateBankAccountCertificate(phone, deviceKey, accountId);
        JWSObject jws = JWSObject.parse(certificate);
        String payload = jws.getPayload().toString();

        // Compute expected hashes
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String expectedPhoneHash = hashAndEncode(phone, digest);
        String expectedDeviceHash = hashAndEncode(deviceKey, digest);
        String expectedAccountHash = hashAndEncode(accountId, digest);

        // Verify payload content
        assertEquals(expectedPhoneHash, JSONObjectUtils.getString(JSONObjectUtils.parse(payload), "phoneHash"));
        assertEquals(expectedDeviceHash, JSONObjectUtils.getString(JSONObjectUtils.parse(payload), "devicePubKeyHash"));
        assertEquals(expectedAccountHash, JSONObjectUtils.getString(JSONObjectUtils.parse(payload), "accountIdHash"));
    }

    @Test
    void testGenerateBankAccountCertificate_NullParameters() {
        assertThrows(IllegalStateException.class, () ->
                certificateCreationService.generateBankAccountCertificate(null, "key", "id"));
        assertThrows(IllegalStateException.class, () ->
                certificateCreationService.generateBankAccountCertificate("123", null, "id"));
        assertThrows(IllegalStateException.class, () ->
                certificateCreationService.generateBankAccountCertificate("123", "key", null));
    }

    @Test
    void testGenerateBankAccountCertificate_EmptyParameters() {
        assertDoesNotThrow(() ->
                certificateCreationService.generateBankAccountCertificate("", "", ""));
    }

    @Test
    void testGenerateBankAccountCertificate_JwkHeader() throws Exception {
        String certificate = certificateCreationService.generateBankAccountCertificate(
                "123", "key", "id");
        JWSObject jws = JWSObject.parse(certificate);
        JWK headerJwk = jws.getHeader().getJWK();

        ECKey expectedPublicKey = (ECKey) JWK.parse(certificateCreationService.serverPublicKeyJson);
        assertEquals(expectedPublicKey.getKeyType(), headerJwk.getKeyType());
        assertEquals(expectedPublicKey.getCurve(), ((ECKey) headerJwk).getCurve());
        assertEquals(expectedPublicKey.getX().toString(), ((ECKey) headerJwk).getX().toString());
        assertEquals(expectedPublicKey.getY().toString(), ((ECKey) headerJwk).getY().toString());
    }

    private String hashAndEncode(String input, MessageDigest digest) {
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        digest.reset();
        return Base64.getEncoder().encodeToString(hash);
    }
}