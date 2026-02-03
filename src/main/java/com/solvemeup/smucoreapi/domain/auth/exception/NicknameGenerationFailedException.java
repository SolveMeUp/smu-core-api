package com.solvemeup.smucoreapi.domain.auth.exception;

import lombok.Getter;

/**
 * 랜덤 닉네임 생성에 실패했을 때 발생하는 내부 예외.
 */
@Getter
public class NicknameGenerationFailedException extends RuntimeException {

    private final int retryLimit;

    public NicknameGenerationFailedException(int retryLimit, Throwable cause) {
        super("Failed to generate unique nickname after " + retryLimit + " attempts.", cause);
        this.retryLimit = retryLimit;
    }
}
