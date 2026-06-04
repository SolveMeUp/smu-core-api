package com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.result;

import com.solvemeup.smucoreapi.domain.judge.common.Verdict;

public record ExecutionResultMessage(
        Long executionId,
        int caseIndex,
        Verdict verdict,
        String arguments,
        String expectedOutput,
        String actualOutput,
        Integer timeUsedMillis,
        Integer memoryUsedKilobytes
) {
}
