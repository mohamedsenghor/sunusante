package dev.sunusante.dmp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class EncryptionUtils {

    private static final Logger log = LoggerFactory.getLogger(EncryptionUtils.class);

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    private final SecretKeySpec keySpec;
    private final IvParameterSpec ivSpec;

    public EncryptionUtils(@Value("${jhipster.security.authentication.jwt.base64-secret}") String secret) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        byte[] finalKey = new byte[16];
        System.arraycopy(keyBytes, 0, finalKey, 0, 16);
        this.keySpec = new SecretKeySpec(finalKey, "AES");

        byte[] iv = new byte[16];
        System.arraycopy(keyBytes, 16, iv, 0, 16);
        this.ivSpec = new IvParameterSpec(iv);
    }

    public String encrypt(String value) {
        if (value == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.error("Encryption failed", e);
            return value;
        }
    }

    public String decrypt(String encryptedValue) {
        if (encryptedValue == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Decryption failed", e);
            return encryptedValue;
        }
    }
}
