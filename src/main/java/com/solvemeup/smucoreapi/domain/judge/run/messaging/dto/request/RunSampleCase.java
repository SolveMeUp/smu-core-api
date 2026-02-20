package com.solvemeup.smucoreapi.domain.judge.run.messaging.dto.request;

public record RunSampleCase(
        int caseIndex,
        String argumentsJson
) {
}
