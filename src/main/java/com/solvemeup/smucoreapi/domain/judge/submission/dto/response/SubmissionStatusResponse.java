package com.solvemeup.smucoreapi.domain.judge.submission.dto.response;

import com.solvemeup.smucoreapi.domain.judge.common.Verdict;
import com.solvemeup.smucoreapi.domain.judge.submission.entity.Submission;
import com.solvemeup.smucoreapi.domain.judge.submission.entity.SubmissionStatus;

public record SubmissionStatusResponse(
        SubmissionStatus status,
        Verdict verdict,
        FailedCase failedCase,
        Integer timeUsedMillis,
        Integer memoryUsedKilobytes
) {
    public record FailedCase(
            int caseIndex,
            String arguments,
            String expectedOutput,
            String actualOutput
    ) {
    }

    public static SubmissionStatusResponse from(Submission submission) {
        return new SubmissionStatusResponse(
                submission.getStatus(),
                submission.getVerdict(),
                toFailedCase(submission),
                submission.getTimeUsedMillis(),
                submission.getMemoryUsedKilobytes()
        );
    }

    private static FailedCase toFailedCase(Submission submission) {
        if (submission.getFailedCaseIndex() == null) {
            return null;
        }
        return new FailedCase(
                submission.getFailedCaseIndex(),
                submission.getArguments(),
                submission.getExpectedOutput(),
                submission.getActualOutput()
        );
    }
}
