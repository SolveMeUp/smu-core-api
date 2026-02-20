package com.solvemeup.smucoreapi.domain.judge.run.messaging.dto.request;

import java.util.List;

public record RunMessage(
        Long runId,

        String functionName,
        List<ParameterSpec> parameters,
        String returnType,

        List<RunSampleCase> testCases,

        int timeLimitMillis,
        int memoryLimitKilobytes,
        String language,
        String sourceCode
) {
}
