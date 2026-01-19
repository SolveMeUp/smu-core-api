package com.solvemeup.smucoreapi.domain.user.exception;

public class NicknameGenerationFailedException extends RuntimeException {
    public NicknameGenerationFailedException(int retryLimit, Throwable cause) {
        super("Failed to generate unique nickname after " + retryLimit + " attempts", cause);
    }
}
