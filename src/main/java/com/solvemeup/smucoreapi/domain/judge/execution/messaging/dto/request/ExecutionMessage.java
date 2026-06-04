package com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.request;

import java.util.List;

public record ExecutionMessage(
        Long runId,

        String functionName,
        List<ParameterSpec> parameters,
        String returnType,

        List<ExecutionSampleCase> testCases,

        int timeLimitMillis,
        int memoryLimitKilobytes,
        String language,
        String sourceCode
) {
}
