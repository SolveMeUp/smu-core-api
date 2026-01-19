package com.solvemeup.smucoreapi.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Auth
    AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH-001", "로그인이 필요합니다."),
    AUTH_FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH-002", "권한이 없습니다."),

    // OAuth2
    OAUTH2_UNSUPPORTED_PROVIDER(HttpStatus.BAD_REQUEST, "OAUTH2-001", "지원하지 않는 OAuth2 Provider 입니다."),
    OAUTH2_INVALID_REGISTRATION_ID(HttpStatus.BAD_REQUEST, "OAUTH2-002", "잘못된 OAuth2 registrationId 입니다."),
    OAUTH2_MISSING_ATTRIBUTE(HttpStatus.INTERNAL_SERVER_ERROR, "OAUTH2-003", "OAuth2 응답 필수 값이 누락되었습니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "유저를 찾을 수 없습니다."),
    NICKNAME_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "USER-002", "닉네임 생성에 실패했습니다."),

    // Common
    COMMON_BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON-001", "잘못된 요청입니다."),
    COMMON_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-002", "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String code, String defaultMessage) {
        this.status = status;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}
