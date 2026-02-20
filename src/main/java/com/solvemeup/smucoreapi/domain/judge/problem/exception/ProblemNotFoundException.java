package com.solvemeup.smucoreapi.domain.judge.problem.exception;

import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.PROBLEM_NOT_FOUND;

public class ProblemNotFoundException extends CustomException {

    public ProblemNotFoundException() {
        super(PROBLEM_NOT_FOUND);
    }
}
