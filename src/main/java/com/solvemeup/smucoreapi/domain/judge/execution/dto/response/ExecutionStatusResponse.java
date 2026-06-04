package com.solvemeup.smucoreapi.domain.judge.execution.dto.response;

import java.util.List;

public record ExecutionStatusResponse(
        String status,
        List<SampleCaseResult> results
) {
}
