package com.solvemeup.smucoreapi.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(
            CustomException e,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = e.getErrorCode();
        String path = request.getRequestURI();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(path, errorCode, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception e,
            HttpServletRequest request
    ) {
        String path = request.getRequestURI();

        return ResponseEntity
                .status(ErrorCode.COMMON_INTERNAL_ERROR.getStatus())
                .body(ErrorResponse.of(ErrorCode.COMMON_INTERNAL_ERROR, path));
    }
}
