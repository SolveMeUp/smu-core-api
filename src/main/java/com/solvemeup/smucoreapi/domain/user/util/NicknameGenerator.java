package com.solvemeup.smucoreapi.domain.user.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * 사용자 닉네임을 자동 생성하는 유틸리티 컴포넌트.
 *
 * <p>첫 글자는 영문자만 사용하며,
 * 이후 문자는 영문자, 숫자, '-', '_' 조합으로 생성된다.
 *
 * <p>닉네임 충돌 가능성을 낮추기 위해 {@link SecureRandom}을 사용한다.
 */
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
