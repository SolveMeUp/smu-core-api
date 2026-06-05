package com.solvemeup.smucoreapi.domain.user.dto.response;

import com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.entity.UserRole;
import com.solvemeup.smucoreapi.domain.user.entity.UserStatus;

import java.time.LocalDateTime;

public record MyProfileResponse(
        Long id,
        OAuth2Provider oauth2Provider,
        String email,
        String nickname,
        String profileImageUrl,
        String githubUrl,
        String techblogUrl,
        int rating,
        UserRole role,
        UserStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        long rank
) {
    public static MyProfileResponse from(User user, long rank) {
        return new MyProfileResponse(
                user.getId(),
                user.getOauth2Provider(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImageUrl(),
                user.getGithubUrl(),
                user.getTechblogUrl(),
                user.getRating(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                rank
        );
    }
}
