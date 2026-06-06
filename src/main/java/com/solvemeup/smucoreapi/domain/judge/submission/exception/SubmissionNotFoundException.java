package com.solvemeup.smucoreapi.domain.judge.submission.exception;

import com.solvemeup.smucoreapi.global.exception.BusinessException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.SUBMISSION_NOT_FOUND;

public class SubmissionNotFoundException extends BusinessException {

    public SubmissionNotFoundException() {
        super(SUBMISSION_NOT_FOUND);
    }
}
