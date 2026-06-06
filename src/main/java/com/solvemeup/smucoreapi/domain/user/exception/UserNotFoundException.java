package com.solvemeup.smucoreapi.domain.user.exception;

import com.solvemeup.smucoreapi.global.exception.BusinessException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.USER_NOT_FOUND;

/**
 * 요청한 사용자(userId)를 찾을 수 없을 때 발생하는 예외.
 */
public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super(USER_NOT_FOUND);
    }
}
