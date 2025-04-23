package com.example.logindemo;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class PasswordManager {

    public static byte[] generateSalt() {
        byte[] salt = new byte[64];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    public static String byteArrayToString(byte[] barray) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(barray);
    }

    public static byte[] stringToByteArray(String str) {
        return Base64.getUrlDecoder().decode(str);
    }

    public static String generatePasswordHash(String password, byte[] salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 512);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return byteArrayToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }

    public static boolean authenticate(String email, String enteredPassword) {
        String storedPasswordHash = DBManager.retrievePassword(email);
        String strSalt = DBManager.retrieveSalt(email);

        if (strSalt == null || strSalt.isEmpty()) {
            return false;
        }

        byte[] byteSalt = stringToByteArray(strSalt);
        String calculatedPasswordHash = generatePasswordHash(enteredPassword, byteSalt);
        return storedPasswordHash.equals(calculatedPasswordHash);
    }
}
