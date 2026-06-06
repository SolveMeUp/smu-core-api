package com.solvemeup.smucoreapi.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException e,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = e.getErrorCode();

        log.warn("BusinessException: code={}, path={}", errorCode.getCode(), request.getRequestURI());

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(request.getRequestURI(), errorCode));
    }

    /**
     * {@code @Valid @RequestBody} DTO 검증 실패.
     *
     * <p>거부된 각 필드명과 사유를 응답에 담아 클라이언트가 어떤 입력이
     * 잘못되었는지 알 수 있게 한다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        List<ErrorResponse.FieldError> errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorResponse.FieldError(error.getField(), error.getDefaultMessage()))
                .toList();

        return ResponseEntity
                .status(COMMON_BAD_REQUEST.getStatus())
                .body(ErrorResponse.of(request.getRequestURI(), COMMON_BAD_REQUEST, errors));
    }

    /**
     * {@code @Validated} 파라미터·path 변수 검증 실패.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException e,
            HttpServletRequest request
    ) {
        List<ErrorResponse.FieldError> errors = e.getConstraintViolations().stream()
                .map(violation -> new ErrorResponse.FieldError(
                        lastNode(violation.getPropertyPath().toString()),
                        violation.getMessage()
                ))
                .toList();

        return ResponseEntity
                .status(COMMON_BAD_REQUEST.getStatus())
                .body(ErrorResponse.of(request.getRequestURI(), COMMON_BAD_REQUEST, errors));
    }

    /**
     * 요청 본문·파라미터 자체가 잘못된 경우.
     *
     * <p>읽을 수 없는 JSON 본문, 필수 파라미터 누락, 파라미터 타입 불일치 등
     * 단일 지점 오류라 필드 목록 없이 공통 메시지로 응답한다.
     */
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(HttpServletRequest request) {
        return ResponseEntity
                .status(COMMON_BAD_REQUEST.getStatus())
                .body(ErrorResponse.of(request.getRequestURI(), COMMON_BAD_REQUEST));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupported(HttpServletRequest request) {
        return ResponseEntity
                .status(COMMON_METHOD_NOT_ALLOWED.getStatus())
                .body(ErrorResponse.of(request.getRequestURI(), COMMON_METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception e,
            HttpServletRequest request
    ) {
        log.error("Unhandled exception at {}", request.getRequestURI(), e);

        return ResponseEntity
                .status(COMMON_INTERNAL_ERROR.getStatus())
                .body(ErrorResponse.of(request.getRequestURI(), COMMON_INTERNAL_ERROR));
    }

    /**
     * 검증 위반 경로(예: {@code updateNickname.nickname})에서 마지막 노드만 필드명으로 추출한다.
     */
    private static String lastNode(String propertyPath) {
        int lastDot = propertyPath.lastIndexOf('.');
        return lastDot >= 0 ? propertyPath.substring(lastDot + 1) : propertyPath;
    }
}
