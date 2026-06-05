package com.solvemeup.smucoreapi.domain.judge.execution.exception;

import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.EXECUTION_NOT_FOUND;

public class ExecutionNotFoundException extends CustomException {

    public ExecutionNotFoundException() {
        super(EXECUTION_NOT_FOUND);
    }
}
