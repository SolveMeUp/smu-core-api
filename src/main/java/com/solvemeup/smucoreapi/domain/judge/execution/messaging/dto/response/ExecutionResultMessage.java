package com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.response;

public record ExecutionResultMessage(
        Long executionId,
        int caseIndex,

        String status,
        String expectedOutput,
        String actualOutput,
        Integer timeUsedMillis,
        Integer memoryUsedKilobytes
) {
}
