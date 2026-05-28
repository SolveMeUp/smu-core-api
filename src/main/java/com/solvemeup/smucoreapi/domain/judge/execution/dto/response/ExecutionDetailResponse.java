package com.solvemeup.smucoreapi.domain.judge.execution.dto.response;

import com.solvemeup.smucoreapi.domain.judge.execution.entity.Execution;
import com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionCaseResult;
import com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ExecutionDetailResponse(
        Long executionId,
        ExecutionStatus status,
        int totalCaseCount,
        int completedCaseCount,
        LocalDateTime finishedAt,
        List<CaseResultDTO> results
) {
    public static ExecutionDetailResponse from(Execution execution, List<ExecutionCaseResult> results) {
        return new ExecutionDetailResponse(
                execution.getId(),
                execution.getStatus(),
                execution.getTotalCaseCount(),
                results.size(),
                execution.getFinishedAt(),
                results.stream()
                        .map(CaseResultDTO::from)
                        .toList()
        );
    }
}
