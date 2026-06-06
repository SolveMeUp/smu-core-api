package com.solvemeup.smucoreapi.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // =========================
    // Auth
    // =========================
    AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH-001", "로그인이 필요합니다."),
    AUTH_FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH-002", "접근 권한이 없습니다."),
    AUTH_BLOCKED_USER(HttpStatus.FORBIDDEN, "AUTH-003", "차단된 계정입니다."),

    // =========================
    // OAuth2
    // =========================
    OAUTH2_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "OAUTH2-001", "OAuth2 로그인에 실패했습니다."),
    OAUTH2_UNSUPPORTED_PROVIDER(HttpStatus.BAD_REQUEST, "OAUTH2-002", "지원하지 않는 OAuth2 Provider입니다."),
    OAUTH2_MISSING_ATTRIBUTE(HttpStatus.INTERNAL_SERVER_ERROR, "OAUTH2-003", "OAuth2 응답 필수 값이 누락되었습니다."),

    // =========================
    // User
    // =========================
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "해당 유저를 찾을 수 없습니다."),
    USER_INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "USER-002", "닉네임이 올바르지 않습니다."),
    USER_NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER-003", "중복된 닉네임입니다."),

    // =========================
    // Problem
    // =========================
    PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND, "PROBLEM-001", "해당 문제를 찾을 수 없습니다."),

    // =========================
    // Submission
    // =========================
    SUBMISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "SUBMISSION-001", "해당 제출을 찾을 수 없습니다."),

    // =========================
    // Execution
    // =========================
    EXECUTION_NOT_FOUND(HttpStatus.NOT_FOUND, "EXECUTION-001", "해당 실행을 찾을 수 없습니다."),

    // =========================
    // Common
    // =========================
    COMMON_BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON-001", "잘못된 요청입니다."),
    COMMON_METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON-002", "지원하지 않는 HTTP 메서드입니다."),
    COMMON_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-003", "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String code, String defaultMessage) {
        this.status = status;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}
