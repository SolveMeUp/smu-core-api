package com.solvemeup.smucoreapi.global.exception.auth;

import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.*;

public class ForbiddenException extends CustomException {

    public ForbiddenException() {
        super(AUTH_FORBIDDEN);
    }

    public ForbiddenException(String message) {
        super(AUTH_FORBIDDEN, message);
    }
}
