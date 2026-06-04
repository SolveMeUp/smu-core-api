package com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.request;

public record ExecutionSampleCase(
        int caseIndex,
        String argumentsJson
) {
}
