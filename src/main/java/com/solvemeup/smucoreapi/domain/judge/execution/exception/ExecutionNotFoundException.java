package com.solvemeup.smucoreapi.domain.judge.execution.exception;

import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.*;

public class ExecutionNotFoundException extends CustomException {

    public ExecutionNotFoundException() {
        super(EXECUTION_NOT_FOUND);
    }
}
