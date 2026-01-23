package com.solvemeup.smucoreapi.domain.auth.exception;

import lombok.Getter;

@Getter
public class NicknameGenerationFailedException extends RuntimeException {

    private final int retryLimit;

    public NicknameGenerationFailedException(int retryLimit, Throwable cause) {
        super("Failed to generate unique nickname after " + retryLimit + " attempts.", cause);
        this.retryLimit = retryLimit;
    }
}
