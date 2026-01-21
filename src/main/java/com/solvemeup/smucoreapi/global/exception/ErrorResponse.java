package com.solvemeup.smucoreapi.global.exception;

import java.time.Instant;

public record ErrorResponse(
        String path,
        String code,
        String message,
        Instant timestamp
) {
    public static ErrorResponse of(String path, ErrorCode errorCode, String message) {
        return new ErrorResponse(
                path,
                errorCode.getCode(),
                message,
                Instant.now()
        );
    }

    public static ErrorResponse of(String path, ErrorCode errorCode) {
        return of(path, errorCode, errorCode.getDefaultMessage());
    }
}
