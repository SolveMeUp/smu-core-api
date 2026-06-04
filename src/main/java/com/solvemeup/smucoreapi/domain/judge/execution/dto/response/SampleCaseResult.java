package com.solvemeup.smucoreapi.domain.judge.execution.dto.response;

public record SampleCaseResult(
        int caseIndex,
        String status,
        String expectedOutput,
        String actualOutput,
        Integer timeUsedMillis,
        Integer memoryUsedKilobytes
) {
}
