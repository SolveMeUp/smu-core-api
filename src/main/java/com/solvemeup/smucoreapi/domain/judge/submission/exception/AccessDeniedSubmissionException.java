package com.solvemeup.smucoreapi.domain.judge.submission.exception;

import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.ACCESS_DENIED_SUBMISSION;

public class AccessDeniedSubmissionException extends CustomException {

    public AccessDeniedSubmissionException() {
        super(ACCESS_DENIED_SUBMISSION);
    }
}
