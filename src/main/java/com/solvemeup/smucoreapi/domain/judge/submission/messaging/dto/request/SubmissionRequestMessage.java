package com.solvemeup.smucoreapi.domain.judge.submission.messaging.dto.request;

import com.solvemeup.smucoreapi.domain.judge.common.Language;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.DataType;

import java.util.List;

public record SubmissionRequestMessage(
        Long submissionId,
        Long problemId,
        String functionName,
        List<SubmissionParameter> parameters,
        DataType returnType,
        int timeLimitMillis,
        int memoryLimitKilobytes,
        Language language,
        String sourceCode
) {
}
