package com.solvemeup.smucoreapi.domain.user.exception;

import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.*;

public class InvalidNicknameException extends CustomException {

    public InvalidNicknameException() {
        super(INVALID_NICKNAME);
    }
}
