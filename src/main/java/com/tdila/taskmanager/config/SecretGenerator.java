package com.tdila.taskmanager.config;

import java.util.Base64;
import java.security.SecureRandom;

public class SecretGenerator {
    public static void main(String[] args) {
        byte[] secret = new byte[32]; // 256 bits for HS256
        new SecureRandom().nextBytes(secret);
        String base64 = Base64.getEncoder().encodeToString(secret);
        System.out.println("Base64 Secret: " + base64);
    }
}