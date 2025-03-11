package com.telerikacademy.web.virtual_wallet.helpers;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TokenGenerator {

    private static final Map<String, Instant> tokenExpiryMap = new HashMap<>();

    public static String generateToken() {

        String token = UUID.randomUUID().toString();
        Instant expiration = Instant.now().plusSeconds(15 * 60);

        tokenExpiryMap.put(token, expiration);

        return token;
    }

    public static boolean isTokenExpired(String token) {
        Instant expiration = tokenExpiryMap.get(token);
        if (expiration == null || Instant.now().isAfter(expiration)) return true;
        return false;
    }

    public static String renewToken(String token) {
        tokenExpiryMap.remove(token);
        return generateToken();
    }
}
