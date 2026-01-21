package com.solvemeup.smucoreapi.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record UpdateMyTechblogUrlRequest(
        @NotBlank(message = "url은 필수입니다.")
        @URL(message = "올바른 URL 형식이 아닙니다.")
        String techblogUrl
) {
}
