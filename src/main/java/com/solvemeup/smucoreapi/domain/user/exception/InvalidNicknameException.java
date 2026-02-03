package com.solvemeup.smucoreapi.domain.user.exception;

import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.*;

/**
 * 닉네임이 도메인 규칙을 만족하지 않을 때 발생하는 예외.
 */
public class InvalidNicknameException extends CustomException {

    public InvalidNicknameException() {
        super(INVALID_NICKNAME);
    }
}
