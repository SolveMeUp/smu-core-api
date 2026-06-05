package com.solvemeup.smucoreapi.domain.auth.exception;

import com.solvemeup.smucoreapi.global.exception.ErrorCode;
import lombok.Getter;

/**
 * OAuth2 로그인 처리 중 발생하는 도메인 예외의 공통 부모.
 *
 * <p>각 예외가 자신의 {@link ErrorCode}를 보유하여, "예외 → 에러 코드" 매핑이
 * 서비스 catch에 흩어지지 않고 예외 자신에게 응집되도록 한다.
 *
 * <p>Spring Security 필터 단계(loadUser)에서 발생하므로 {@code CustomException}이 아닌
 * {@link RuntimeException}을 상속한다. (GlobalExceptionHandler에 도달하지 못하고
 * 인증 실패 핸들러 흐름으로 처리된다.)
 */
@Getter
public abstract class OAuth2LoginException extends RuntimeException {

    private final ErrorCode errorCode;

    protected OAuth2LoginException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
