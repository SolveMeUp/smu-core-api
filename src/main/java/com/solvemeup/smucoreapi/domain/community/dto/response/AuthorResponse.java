package com.solvemeup.smucoreapi.domain.community.dto.response;

import com.solvemeup.smucoreapi.domain.user.entity.UserEntity;
import lombok.Builder;

@Builder
public record AuthorResponse(
        Long id,
        String nickname,
        String profileImageUrl
) {
    public static AuthorResponse from(UserEntity user) {
        return AuthorResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
