package com.solvemeup.smucoreapi.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        String path,
        String code,
        String message,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<FieldError> errors,
        Instant timestamp
) {
    public record FieldError(String field, String reason) {
    }

    public static ErrorResponse of(String path, ErrorCode errorCode) {
        return new ErrorResponse(
                path,
                errorCode.getCode(),
                errorCode.getDefaultMessage(),
                List.of(),
                Instant.now()
        );
    }

    public static ErrorResponse of(String path, ErrorCode errorCode, List<FieldError> errors) {
        return new ErrorResponse(
                path,
                errorCode.getCode(),
                errorCode.getDefaultMessage(),
                errors,
                Instant.now()
        );
    }
}
