package com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.response;

import com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionCaseResultStatus;

public record ExecutionResultMessage(
        Long executionId,
        int caseIndex,
        ExecutionCaseResultStatus status,
        String actualOutput,
        Integer timeUsedMillis,
        Integer memoryUsedKilobytes
) {
}
