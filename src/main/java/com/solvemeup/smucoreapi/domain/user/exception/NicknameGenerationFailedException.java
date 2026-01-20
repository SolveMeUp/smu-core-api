package com.solvemeup.smucoreapi.domain.user.exception;

import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.*;

public class NicknameGenerationFailedException extends CustomException {

    public NicknameGenerationFailedException(Throwable cause) {
        super(NICKNAME_GENERATION_FAILED, cause);
    }
}
