package com.solvemeup.smucoreapi.domain.judge.execution.dto.response;

import com.solvemeup.smucoreapi.domain.judge.common.Verdict;
import com.solvemeup.smucoreapi.domain.judge.execution.entity.Execution;
import com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionResult;
import com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionStatus;

import java.util.List;

public record ExecutionStatusResponse(
        ExecutionStatus status,
        List<CaseResponse> results
) {
    public record CaseResponse(
            int caseIndex,
            Verdict verdict,
            String arguments,
            String expectedOutput,
            String actualOutput,
            Integer timeUsedMillis,
            Integer memoryUsedKilobytes
    ) {
        public static CaseResponse from(ExecutionResult result) {
            return new CaseResponse(
                    result.getCaseIndex(),
                    result.getVerdict(),
                    result.getArguments(),
                    result.getExpectedOutput(),
                    result.getActualOutput(),
                    result.getTimeUsedMillis(),
                    result.getMemoryUsedKilobytes()
            );
        }
    }

    public static ExecutionStatusResponse from(Execution execution, List<ExecutionResult> results) {
        return new ExecutionStatusResponse(
                execution.getStatus(),
                results.stream()
                        .map(CaseResponse::from)
                        .toList()
        );
    }
}
