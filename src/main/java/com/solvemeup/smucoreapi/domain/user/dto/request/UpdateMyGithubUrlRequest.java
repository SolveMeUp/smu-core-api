package com.solvemeup.smucoreapi.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record UpdateMyGithubUrlRequest(
        @NotBlank(message = "url은 필수입니다.")
        @Size(max = 2083, message = "url은 2083자를 초과할 수 없습니다.")
        @URL(message = "올바른 URL 형식이 아닙니다.")
        String githubUrl
) {
}
