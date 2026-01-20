package com.solvemeup.smucoreapi.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateMyNicknameRequestDTO(
        @NotBlank(message = "닉네임은 필수입니다.")
        @Size(min = 4, max = 20, message = "닉네임은 최소 4자에서 최대 20자까지 가능합니다.")
        String nickname
) {
}
