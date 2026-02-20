package com.solvemeup.smucoreapi.domain.judge.submission.exception;

import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.SUBMISSION_RESULT_NOT_FOUND;

public class SubmissionResultNotFoundException extends CustomException {

    public SubmissionResultNotFoundException() {
        super(SUBMISSION_RESULT_NOT_FOUND);
    }
}
