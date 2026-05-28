package com.solvemeup.smucoreapi.domain.judge.submission.exception;

import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.SUBMISSION_NOT_FOUND;

public class SubmissionNotFoundException extends CustomException {

    public SubmissionNotFoundException() {
        super(SUBMISSION_NOT_FOUND);
    }
}
