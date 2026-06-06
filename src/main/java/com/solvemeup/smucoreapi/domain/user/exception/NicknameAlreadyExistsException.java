package com.solvemeup.smucoreapi.domain.user.exception;

import com.solvemeup.smucoreapi.global.exception.BusinessException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.USER_NICKNAME_ALREADY_EXISTS;

/**
 * 이미 사용 중인 닉네임으로 변경을 시도했을 때 발생하는 예외.
 */
public class NicknameAlreadyExistsException extends BusinessException {

    public NicknameAlreadyExistsException() {
        super(USER_NICKNAME_ALREADY_EXISTS);
    }
}
