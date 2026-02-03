package com.solvemeup.smucoreapi.domain.user.exception;

import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.*;

/**
 * 이미 사용 중인 닉네임으로 변경을 시도했을 때 발생하는 예외.
 */
public class NicknameAlreadyExistsException extends CustomException {

    public NicknameAlreadyExistsException(String nickname) {
        super(NICKNAME_ALREADY_EXISTS, nickname + " already exists!");
    }
}
