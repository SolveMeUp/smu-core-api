package com.solvemeup.smucoreapi.domain.judge.submission.dto.request;

import com.solvemeup.smucoreapi.domain.judge.submission.entity.Language;

public record SubmitSolutionRequest(Language language, String sourceCode) {
}
