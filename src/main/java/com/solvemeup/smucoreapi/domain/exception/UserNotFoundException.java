package com.solvemeup.smucoreapi.domain.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long userId) {
        super("사용자를 찾을 수 없습니다. userId=" + userId);
    }

    public UserNotFoundException(String nickname) {
        super("사용자를 찾을 수 없습니다. nickname=" + nickname);
    }
}
