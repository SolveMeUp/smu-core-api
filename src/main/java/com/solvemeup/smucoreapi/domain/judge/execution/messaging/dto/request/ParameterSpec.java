package com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.request;

import com.solvemeup.smucoreapi.domain.judge.problem.entity.ValueType;

public record ParameterSpec(
        String name,
        ValueType type
) {
}
