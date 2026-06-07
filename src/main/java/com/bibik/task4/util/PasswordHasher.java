package com.bibik.task4.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class PasswordHasher {

    private static final String ALGORITHM = "SHA-256";

    private PasswordHasher() {}

    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] hashBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found", e);
        }
    }

    public static boolean verify(String password, String hashedPassword) {
        return hash(password).equals(hashedPassword);
    }
}
