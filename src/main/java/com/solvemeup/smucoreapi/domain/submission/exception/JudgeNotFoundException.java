package com.solvemeup.smucoreapi.domain.submission.exception;

import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.JUDGE_NOT_FOUND;

public class JudgeNotFoundException extends CustomException {

    public JudgeNotFoundException() {
        super(JUDGE_NOT_FOUND);
    }
}
