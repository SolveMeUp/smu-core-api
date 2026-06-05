package com.solvemeup.smucoreapi.domain.user.dto.response;

import com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.entity.UserRole;
import com.solvemeup.smucoreapi.domain.user.repository.UserRankingProjection;

public record UserProfileResponse(
        Long id,
        OAuth2Provider oauth2Provider,
        String nickname,
        String profileImageUrl,
        String githubUrl,
        String techblogUrl,
        int rating,
        UserRole role,
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
                p.getRole(),
                p.getRank()
        );
    }
}
