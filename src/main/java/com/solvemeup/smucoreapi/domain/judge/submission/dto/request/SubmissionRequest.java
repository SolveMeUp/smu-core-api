package com.solvemeup.smucoreapi.domain.judge.submission.dto.request;

import com.solvemeup.smucoreapi.domain.judge.common.Language;

public record SubmissionRequest(Long problemId, Language language, String sourceCode) {
}
