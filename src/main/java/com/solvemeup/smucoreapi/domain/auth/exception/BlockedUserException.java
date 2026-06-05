package com.solvemeup.smucoreapi.domain.auth.exception;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.AUTH_BLOCKED_USER;

/**
 * 차단(BLOCKED)된 사용자가 로그인을 시도한 경우 발생하는 예외.
 */
public class BlockedUserException extends OAuth2LoginException {

    public BlockedUserException() {
        super(AUTH_BLOCKED_USER, "Blocked user tried to login");
    }
}
