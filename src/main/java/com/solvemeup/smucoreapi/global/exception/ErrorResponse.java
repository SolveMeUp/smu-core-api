package com.solvemeup.smucoreapi.global.exception;

import java.time.Instant;

public record ErrorResponse(
        int status,
        String path,
        String message,
        Instant timestamp
) {
    public static ErrorResponse of(int status, String path, String message) {
        return new ErrorResponse(status, path, message, Instant.now());
    }
}
