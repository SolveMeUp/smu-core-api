package com.solvemeup.smucoreapi.global.exception.auth;

import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.*;

public class UnauthorizedException extends CustomException {

    public UnauthorizedException() {
        super(AUTH_UNAUTHORIZED);
    }

    public UnauthorizedException(String message) {
        super(AUTH_UNAUTHORIZED, message);
    }
}
