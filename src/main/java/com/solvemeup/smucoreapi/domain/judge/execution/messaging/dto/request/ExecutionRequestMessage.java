package com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.request;

import com.solvemeup.smucoreapi.domain.judge.common.Language;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.DataType;

import java.util.List;

public record ExecutionRequestMessage(
        Long executionId,
        Long problemId,
        String functionName,
        List<ExecutionParameter> parameters,
        DataType returnType,
        List<ExecutionTestCase> testCases,
        int timeLimitMillis,
        int memoryLimitKilobytes,
        Language language,
        String sourceCode
) {
}
