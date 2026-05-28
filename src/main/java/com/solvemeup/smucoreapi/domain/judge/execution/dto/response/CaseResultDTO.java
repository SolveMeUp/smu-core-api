package com.solvemeup.smucoreapi.domain.judge.execution.dto.response;

import com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionCaseResult;
import com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionCaseResultStatus;

public record CaseResultDTO(
        int caseIndex,
        ExecutionCaseResultStatus status,
        String expectedOutput,
        String actualOutput,
        Integer timeUsedMillis,
        Integer memoryUsedKilobytes
) {
    public static CaseResultDTO from(ExecutionCaseResult result) {
        return new CaseResultDTO(
                result.getCaseIndex(),
                result.getStatus(),
                result.getExpectedOutput(),
                result.getActualOutput(),
                result.getTimeUsedMillis(),
                result.getMemoryUsedKilobytes()
        );
    }
}
