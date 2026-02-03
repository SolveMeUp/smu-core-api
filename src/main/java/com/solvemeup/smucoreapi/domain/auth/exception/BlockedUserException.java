package com.solvemeup.smucoreapi.domain.auth.exception;

/**
 * 차단된 사용자가 인증 대상이 되었을 때 발생하는 내부 예외.
 *
 * <p>이 예외는 도메인/인증 내부 로직에서만 사용되며,
 * Security 레이어에서 {@link org.springframework.security.core.AuthenticationException}
 * 으로 변환되어 처리된다.
 */
public class BlockedUserException extends RuntimeException {

    public BlockedUserException() {
        super("Blocked user tried to login");
    }
}
