package com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.request;

public record ExecutionTestCase(
        int caseIndex,
        String arguments,
        String expectedOutput
) {
}
