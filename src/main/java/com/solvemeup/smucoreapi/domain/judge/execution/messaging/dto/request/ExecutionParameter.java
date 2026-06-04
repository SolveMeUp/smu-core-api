package com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.request;

import com.solvemeup.smucoreapi.domain.judge.problem.entity.DataType;

public record ExecutionParameter(
        String name,
        DataType type
) {
}
