package com.solvemeup.smucoreapi.domain.judge.execution.dto.request;

import com.solvemeup.smucoreapi.domain.judge.submission.entity.Language;

public record ExecutionCreateRequest(Language language, String sourceCode) {
}
