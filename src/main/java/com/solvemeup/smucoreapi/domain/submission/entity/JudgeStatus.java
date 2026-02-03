package com.solvemeup.smucoreapi.domain.submission.entity;

public enum JudgeStatus {
    PENDING, RUNNING, DONE;

    public boolean isDone() {
        return this == DONE;
    }
}
