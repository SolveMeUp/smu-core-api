package com.solvemeup.smucoreapi.domain.judge.submission.messaging.dto.request;

import com.solvemeup.smucoreapi.domain.judge.problem.entity.DataType;

public record SubmissionParameter(
        String name,
        DataType type
) {
}
