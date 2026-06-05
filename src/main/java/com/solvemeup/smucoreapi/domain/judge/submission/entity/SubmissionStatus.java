package com.solvemeup.smucoreapi.domain.judge.submission.entity;

public enum SubmissionStatus {
    PENDING, DONE;

    public boolean isDone() {
        return this == DONE;
    }
}
