package com.solvemeup.smucoreapi.domain.user.exception;

import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.*;

public class UserNotFoundException extends CustomException {

    public UserNotFoundException(Long userId) {
        super(USER_NOT_FOUND, "User not found: userId=" + userId);
    }

    public UserNotFoundException(String nickname) {
        super(USER_NOT_FOUND, "User not found: nickname=" + nickname);
    }
}
