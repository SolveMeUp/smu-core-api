package com.solvemeup.smucoreapi.domain.user.dto.response;

import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.enums.Role;
import com.solvemeup.smucoreapi.domain.user.enums.Status;
import com.solvemeup.smucoreapi.domain.user.repository.projection.UserRankingProjection;

public record UserProfileResponseDTO(
        String nickname,
        String profileImageUrl,
        String githubUrl,
        String techblogUrl,
        int rating,
        Role role,
        Status status,
        long rank
) {
    public static UserProfileResponseDTO from(User user, long rank) {
        return new UserProfileResponseDTO(
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

    public static UserProfileResponseDTO from(UserRankingProjection p) {
        return new UserProfileResponseDTO(
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
