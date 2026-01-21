package com.solvemeup.smucoreapi.domain.user.dto.response;

import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.enums.OAuth2Provider;
import com.solvemeup.smucoreapi.domain.user.enums.Role;
import com.solvemeup.smucoreapi.domain.user.enums.Status;
import com.solvemeup.smucoreapi.domain.user.repository.projection.UserRankingProjection;

public record UserProfileResponse(
        Long id,
        OAuth2Provider oauth2Provider,
        String nickname,
        String profileImageUrl,
        String githubUrl,
        String techblogUrl,
        int rating,
        Role role,
        Status status,
        long rank
) {
    public static UserProfileResponse from(User user, long rank) {
        return new UserProfileResponse(
                user.getId(),
                user.getOauth2Provider(),
                user.getNickname(),
                user.getProfileImageUrl(),
                user.getGithubUrl(),
                user.getTechblogUrl(),
                user.getRating(),
                user.getRole(),
                user.getStatus(),
                rank
        );
    }

    public static UserProfileResponse from(UserRankingProjection p) {
        return new UserProfileResponse(
                p.getId(),
                p.getOauth2Provider(),
                p.getNickname(),
                p.getProfileImageUrl(),
                p.getGithubUrl(),
                p.getTechblogUrl(),
                p.getRating(),
                Role.valueOf(p.getRole()),
                Status.valueOf(p.getStatus()),
                p.getRank()
        );
    }
}
