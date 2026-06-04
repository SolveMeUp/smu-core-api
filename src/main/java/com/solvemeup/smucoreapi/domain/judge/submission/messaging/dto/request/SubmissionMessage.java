package com.solvemeup.smucoreapi.domain.judge.submission.messaging.dto.request;

import com.solvemeup.smucoreapi.domain.judge.problem.entity.FunctionParameter;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.DataType;
import com.solvemeup.smucoreapi.domain.judge.submission.entity.Language;

import java.util.List;

public record SubmissionMessage(
        Long submissionResultId,
        Long submissionId,
        Long problemId,

        String functionName,
        List<FunctionParameter> parameters,
        DataType returnType,

        int timeLimitMillis,
        int memoryLimitKilobytes,
        Language language,
        String sourceCode
) {
}
