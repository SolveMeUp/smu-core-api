package com.solvemeup.smucoreapi.domain.user.dto.response;

import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.enums.OAuth2Provider;
import com.solvemeup.smucoreapi.domain.user.enums.Role;
import com.solvemeup.smucoreapi.domain.user.enums.Status;

import java.time.Instant;

public record MyProfileResponseDTO(
        Long id,
        OAuth2Provider oAuth2Provider,
        String email,
        String nickname,
        String profileImageUrl,
        String githubUrl,
        String techblogUrl,
        int rating,
        Role role,
        Instant createdAt,
        Status status,
        long rank
) {
    public static MyProfileResponseDTO from(User user, long rank) {
        return new MyProfileResponseDTO(
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
                user.getStatus(),
                rank
        );
    }
}
