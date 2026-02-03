package com.solvemeup.smucoreapi.domain.user.dto.response;

import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider;
import com.solvemeup.smucoreapi.domain.user.entity.Role;
import com.solvemeup.smucoreapi.domain.user.entity.Status;

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
        Role role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Status status,
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
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getStatus(),
                rank
        );
    }
}
