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

    private String nickname;
    private String profileImageUrl;
    private String githubUrl;
    private String techblogUrl;
    private int rating;
    private Role role;
    private Status status;

    public static UserProfileResponseDTO from(User user) {
        return UserProfileResponseDTO.builder()
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .githubUrl(user.getGithubUrl())
                .techblogUrl(user.getTechblogUrl())
                .rating(user.getRating())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }
}
