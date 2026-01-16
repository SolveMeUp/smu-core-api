package com.solvemeup.smucoreapi.domain.user.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long userId) {
        super("User not found: userId=" + userId);
    }

    public UserNotFoundException(String nickname) {
        super("User not found: nickname=" + nickname);
    }
}
