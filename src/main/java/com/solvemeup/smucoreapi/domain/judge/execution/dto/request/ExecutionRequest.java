package com.solvemeup.smucoreapi.domain.judge.execution.dto.request;

import com.solvemeup.smucoreapi.domain.judge.common.Language;

public record ExecutionRequest(Long problemId, Language language, String sourceCode) {
}
