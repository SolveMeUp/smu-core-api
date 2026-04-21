package com.solvemeup.smucoreapi.domain.judge.submission.dto.response;

import com.solvemeup.smucoreapi.domain.judge.submission.entity.SubmissionResult;
import com.solvemeup.smucoreapi.domain.judge.submission.entity.SubmissionResultResult;
import com.solvemeup.smucoreapi.domain.judge.submission.entity.SubmissionResultStatus;

public record SubmissionResultResponse(
        SubmissionResultStatus status,
        SubmissionResultResult result,
        Integer timeUsedMillis,
        Integer memoryUsedKilobytes
) {
    public static SubmissionResultResponse from(SubmissionResult submissionResult) {
        return new SubmissionResultResponse(
                submissionResult.getStatus(),
                submissionResult.getResult(),
                submissionResult.getTimeUsedMillis(),
                submissionResult.getMemoryUsedKilobytes()
        );
    }
}
