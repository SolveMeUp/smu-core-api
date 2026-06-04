package com.solvemeup.smucoreapi.domain.judge.submission.messaging.dto.result;

import com.solvemeup.smucoreapi.domain.judge.common.Verdict;

public record SubmissionResultMessage(
        Long submissionId,
        Verdict verdict,
        Integer failedCaseIndex,
        String arguments,
        String expectedOutput,
        String actualOutput,
        Integer timeUsedMillis,
        Integer memoryUsedKilobytes
) {
}
