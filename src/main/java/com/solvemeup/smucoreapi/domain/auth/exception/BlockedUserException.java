package com.solvemeup.smucoreapi.domain.auth.exception;

import lombok.Getter;

@Getter
public class BlockedUserException extends RuntimeException {

    private final Long userId;

    public BlockedUserException(Long userId) {
        super("Blocked user tried to login. userId=" + userId);
        this.userId = userId;
    }
}
