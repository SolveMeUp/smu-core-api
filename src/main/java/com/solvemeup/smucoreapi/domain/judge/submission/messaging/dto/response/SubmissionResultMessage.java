package com.solvemeup.smucoreapi.domain.judge.submission.messaging.dto.response;

import com.solvemeup.smucoreapi.domain.judge.submission.entity.SubmissionCaseResultStatus;

import java.util.List;

public record SubmissionResultMessage(
        Long submissionResultId,
        Long submissionId,
        Long problemId,

        SubmissionCaseResultStatus result,
        Integer failedTestIndex,
        Object expectedOutput,
        Object actualOutput,
        List<Object> arguments,
        Integer timeUsedMillis,
        Integer memoryUsedKilobytes
) {
}
