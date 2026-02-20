package com.solvemeup.smucoreapi.domain.judge.run.dto.response;

import java.util.List;

public record RunStatusResponse(
        String status,
        List<SampleCaseResult> results
) {
}
