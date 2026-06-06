package com.solvemeup.smucoreapi.domain.user.exception;

import com.solvemeup.smucoreapi.global.exception.BusinessException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.USER_INVALID_NICKNAME;

/**
 * 닉네임이 도메인 규칙을 만족하지 않을 때 발생하는 예외.
 */
public class InvalidNicknameException extends BusinessException {

    public InvalidNicknameException() {
        super(USER_INVALID_NICKNAME);
    }
}
