package com.example.logindemo;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class PasswordManager {

    public static byte[] generateSalt() {
        byte[] salt = new byte[32];
        new java.util.Random().nextBytes(salt);
        return salt;
    }

    public static String byteArrayToString(byte[] barray) {
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(barray);
    }

    public static byte[] stringToByteArray(String str) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        return decoder.decode(str);
    }

    public static String generatePasswordHash(String password, byte[] salt) {
        byte[] passwordHash = null;
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 512);
            passwordHash = factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return byteArrayToString(passwordHash);
    }

    public static boolean authenticate(String email, String enteredPassword) {
        String storedPasswordHash = DBManager.retrievePassword(email);
        String strSalt = DBManager.retrieveSalt(email);
        byte[] byteSalt = stringToByteArray(strSalt);
        String calculatedPasswordHash = generatePasswordHash(enteredPassword, byteSalt);

        return MessageDigest.isEqual(
                storedPasswordHash.getBytes(StandardCharsets.UTF_8),
                calculatedPasswordHash.getBytes(StandardCharsets.UTF_8)
        );
    }
}
