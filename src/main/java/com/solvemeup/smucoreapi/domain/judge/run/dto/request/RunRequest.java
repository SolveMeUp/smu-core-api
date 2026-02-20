package com.solvemeup.smucoreapi.domain.judge.run.dto.request;

import com.solvemeup.smucoreapi.domain.judge.submission.entity.Language;

public record RunRequest(Language language, String sourceCode) {
}
