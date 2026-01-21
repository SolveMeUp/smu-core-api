package com.solvemeup.smucoreapi.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        String path = request.getRequestURI();

        return ResponseEntity
                .status(ErrorCode.COMMON_BAD_REQUEST.getStatus())
                .body(ErrorResponse.of(path, ErrorCode.COMMON_BAD_REQUEST));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException e,
            HttpServletRequest request
    ) {
        String path = request.getRequestURI();

        return ResponseEntity
                .status(ErrorCode.COMMON_BAD_REQUEST.getStatus())
                .body(ErrorResponse.of(path, ErrorCode.COMMON_BAD_REQUEST));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestParam(
            MissingServletRequestParameterException e,
            HttpServletRequest request
    ) {
        String path = request.getRequestURI();

        return ResponseEntity
                .status(ErrorCode.COMMON_BAD_REQUEST.getStatus())
                .body(ErrorResponse.of(path, ErrorCode.COMMON_BAD_REQUEST));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException e,
            HttpServletRequest request
    ) {
        String path = request.getRequestURI();

        return ResponseEntity
                .status(ErrorCode.COMMON_METHOD_NOT_ALLOWED.getStatus())
                .body(ErrorResponse.of(path, ErrorCode.COMMON_METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception e,
            HttpServletRequest request
    ) {
        String path = request.getRequestURI();

        return ResponseEntity
                .status(ErrorCode.COMMON_INTERNAL_ERROR.getStatus())
                .body(ErrorResponse.of(path, ErrorCode.COMMON_INTERNAL_ERROR));
    }
}
