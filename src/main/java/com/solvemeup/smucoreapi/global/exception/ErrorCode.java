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
    AUTH_INVALID_SESSION(HttpStatus.UNAUTHORIZED, "AUTH-003", "세션이 유효하지 않습니다."),

    // =========================
    // OAuth2
    // =========================
    OAUTH2_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "OAUTH2-001", "OAuth2 로그인에 실패했습니다."),
    OAUTH2_UNSUPPORTED_PROVIDER(HttpStatus.BAD_REQUEST, "OAUTH2-002", "지원하지 않는 OAuth2 Provider 입니다."),
    OAUTH2_INVALID_REGISTRATION_ID(HttpStatus.BAD_REQUEST, "OAUTH2-003", "잘못된 OAuth2 registrationId 입니다."),
    OAUTH2_MISSING_ATTRIBUTE(HttpStatus.INTERNAL_SERVER_ERROR, "OAUTH2-004", "OAuth2 응답 필수 값이 누락되었습니다."),

    // =========================
    // User
    // =========================
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "해당 유저를 찾을 수 없습니다."),
    NICKNAME_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "USER-002", "닉네임 생성에 실패했습니다."),
    INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "USER-003", "닉네임이 올바르지 않습니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER-004", "중복된 닉네임입니다."),
    INACTIVE_USER(HttpStatus.FORBIDDEN, "USER-005", "차단된 회원입니다."),
    USER_DELETED(HttpStatus.FORBIDDEN, "USER-006", "탈퇴한 회원입니다."),
    USER_ANONYMIZED(HttpStatus.FORBIDDEN, "USER-007", "익명화된 회원입니다."),

    // =========================
    // Common
    // =========================
    COMMON_BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON-001", "잘못된 요청입니다."),
    COMMON_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-002", "서버 오류가 발생했습니다."),
    COMMON_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "COMMON-003", "요청 값이 올바르지 않습니다."),
    COMMON_JSON_PARSE_ERROR(HttpStatus.BAD_REQUEST, "COMMON-004", "요청 JSON 형식이 올바르지 않습니다."),
    COMMON_METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON-005", "지원하지 않는 HTTP 메서드입니다."),
    COMMON_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON-006", "요청한 리소스를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String code, String defaultMessage) {
        this.status = status;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}
