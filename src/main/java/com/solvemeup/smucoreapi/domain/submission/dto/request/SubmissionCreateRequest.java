package com.solvemeup.smucoreapi.domain.submission.dto.request;

import com.solvemeup.smucoreapi.domain.submission.entity.Language;

public record SubmissionCreateRequest(Language language, String sourceCode) {
}
