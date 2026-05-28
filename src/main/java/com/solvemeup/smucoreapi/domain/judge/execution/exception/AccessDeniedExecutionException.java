package com.solvemeup.smucoreapi.domain.judge.execution.exception;

import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.ACCESS_DENIED_EXECUTION;

public class AccessDeniedExecutionException extends CustomException {

    public AccessDeniedExecutionException() {
        super(ACCESS_DENIED_EXECUTION);
    }
}
