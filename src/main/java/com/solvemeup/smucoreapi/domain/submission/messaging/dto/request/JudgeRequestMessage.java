package com.solvemeup.smucoreapi.domain.submission.messaging.dto.request;

import com.solvemeup.smucoreapi.domain.submission.entity.Language;

public record JudgeRequestMessage(
        Long judgeId,
        Long submissionId,
        Long problemId,
        int timeLimitMillis,
        int memoryLimitMegabytes,
        Language language,
        String sourceCode
) {
}
