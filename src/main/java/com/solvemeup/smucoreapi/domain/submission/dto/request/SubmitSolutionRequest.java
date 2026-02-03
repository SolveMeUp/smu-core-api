package com.solvemeup.smucoreapi.domain.submission.dto.request;

import com.solvemeup.smucoreapi.domain.submission.entity.Language;

public record SubmitSolutionRequest(Language language, String sourceCode) {
}
