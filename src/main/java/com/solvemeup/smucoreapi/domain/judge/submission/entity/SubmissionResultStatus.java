package com.solvemeup.smucoreapi.domain.judge.submission.entity;

public enum SubmissionResultStatus {
    PENDING, DONE;

    public boolean isDone() {
        return this == DONE;
    }
}
