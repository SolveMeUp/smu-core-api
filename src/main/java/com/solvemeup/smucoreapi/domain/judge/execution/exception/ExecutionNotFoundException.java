package com.solvemeup.smucoreapi.domain.judge.execution.exception;

import com.solvemeup.smucoreapi.global.exception.BusinessException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.EXECUTION_NOT_FOUND;

public class ExecutionNotFoundException extends BusinessException {

    public ExecutionNotFoundException() {
        super(EXECUTION_NOT_FOUND);
    }
}
