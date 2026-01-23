package com.solvemeup.smucoreapi.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(
            CustomException e,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(request.getRequestURI(), errorCode, e.getMessage()));
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            Exception e,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(ErrorCode.COMMON_BAD_REQUEST.getStatus())
                .body(ErrorResponse.of(request.getRequestURI(), ErrorCode.COMMON_BAD_REQUEST));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(ErrorCode.COMMON_METHOD_NOT_ALLOWED.getStatus())
                .body(ErrorResponse.of(request.getRequestURI(), ErrorCode.COMMON_METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception e,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(ErrorCode.COMMON_INTERNAL_ERROR.getStatus())
                .body(ErrorResponse.of(request.getRequestURI(), ErrorCode.COMMON_INTERNAL_ERROR));
    }
}
