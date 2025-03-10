package com.telerikacademy.web.virtual_wallet.helpers;

import java.util.UUID;

public class TokenGenerator {
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
