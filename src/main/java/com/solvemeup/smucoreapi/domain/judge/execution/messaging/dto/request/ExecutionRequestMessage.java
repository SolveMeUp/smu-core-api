package com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.request;

import com.solvemeup.smucoreapi.domain.judge.execution.entity.Execution;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.SampleTestCase;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.ValueType;
import com.solvemeup.smucoreapi.domain.judge.submission.entity.Language;

import java.util.List;

public record ExecutionRequestMessage(
        Long executionId,

        String functionName,
        List<ParameterSpec> parameters,
        ValueType returnType,

        List<ExecutionTestCase> testCases,

        int timeLimitMillis,
        int memoryLimitKilobytes,
        Language language,
        String sourceCode
) {
    public static ExecutionRequestMessage from(Execution execution, Problem problem, List<SampleTestCase> sampleTestCases) {
        return new ExecutionRequestMessage(
                execution.getId(),
                problem.getFunctionName(),
                problem.getParameters().stream()
                        .map(p -> new ParameterSpec(p.name(), p.type()))
                        .toList(),
                problem.getReturnType(),
                sampleTestCases.stream()
                        .map(sc -> new ExecutionTestCase(
                                sc.getOrderIndex(),
                                sc.getArguments(),
                                sc.getExpectedOutput()
                        ))
                        .toList(),
                problem.getTimeLimitMillis(),
                problem.getMemoryLimitKilobytes(),
                execution.getLanguage(),
                execution.getSourceCode()
        );
    }
}
