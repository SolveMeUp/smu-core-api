package com.solvemeup.smucoreapi.domain.user.exception;

import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.*;

public class NicknameAlreadyExistsException extends CustomException {

    public NicknameAlreadyExistsException() {
        super(NICKNAME_ALREADY_EXISTS);
    }
}
