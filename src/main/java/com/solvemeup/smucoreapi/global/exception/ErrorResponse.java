package com.solvemeup.smucoreapi.global.exception;

import java.time.Instant;

public record ErrorResponse(
        String code,
        String message,
        Instant timestamp,
        String path
) {
    public static ErrorResponse of(ErrorCode errorCode, String message, String path) {
        return new ErrorResponse(
                errorCode.getCode(),
                message,
                Instant.now(),
                path
        );
    }

    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return of(errorCode, errorCode.getDefaultMessage(), path);
    }
}
