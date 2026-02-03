package com.solvemeup.smucoreapi.domain.submission.messaging.dto.response;

import com.solvemeup.smucoreapi.domain.submission.entity.JudgeResult;

public record JudgeResponseMessage(
        Long judgeId,
        Long submissionId,
        JudgeResult result,
        Integer timeUsedMillis,
        Integer memoryUsedMegabytes
) {
}
