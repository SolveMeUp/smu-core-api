package com.solvemeup.smucoreapi.domain.user.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class NicknameGenerator {

    private static final String FIRST_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String REST_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
    private static final int LENGTH = 12;

    private final SecureRandom random = new SecureRandom();

    public String generate() {
        StringBuilder sb = new StringBuilder(LENGTH);

        sb.append(FIRST_CHARS.charAt(random.nextInt(FIRST_CHARS.length())));

        for (int i = 1; i < LENGTH; i++) {
            sb.append(REST_CHARS.charAt(random.nextInt(REST_CHARS.length())));
        }

        return sb.toString();
    }
}
