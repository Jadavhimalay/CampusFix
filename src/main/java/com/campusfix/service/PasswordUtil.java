package com.campusfix.service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

public final class PasswordUtil {

    private static final int ITERATIONS = 600_000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    private PasswordUtil() {
    }

    public static String hashPassword(String password)
            throws GeneralSecurityException {

        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);

        byte[] hash = generateHash(password, salt);

        return ITERATIONS + ":" +
                Base64.getEncoder().encodeToString(salt) + ":" +
                Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(
            String password, String storedHash)
            throws GeneralSecurityException {

        String[] parts = storedHash.split(":");
        if (parts.length != 3) {
            return false;
        }

        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = Base64.getDecoder().decode(parts[1]);
        byte[] expectedHash = Base64.getDecoder().decode(parts[2]);

        byte[] actualHash = generateHash(password, salt, iterations);

        if (actualHash.length != expectedHash.length) {
            return false;
        }

        int difference = 0;
        for (int i = 0; i < actualHash.length; i++) {
            difference |= actualHash[i] ^ expectedHash[i];
        }

        return difference == 0;
    }

    private static byte[] generateHash(String password, byte[] salt)
            throws GeneralSecurityException {
        return generateHash(password, salt, ITERATIONS);
    }

    private static byte[] generateHash(
            String password, byte[] salt, int iterations)
            throws GeneralSecurityException {

        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(), salt, iterations, KEY_LENGTH);

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return factory.generateSecret(spec).getEncoded();
        } finally {
            spec.clearPassword();
        }
    }
}
