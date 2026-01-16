package com.solvemeup.smucoreapi.domain.user.dto.response;

import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.enums.Role;
import com.solvemeup.smucoreapi.domain.user.enums.Status;

public record MyProfileResponseDTO(
        String email,
        String nickname,
        String profileImageUrl,
        String githubUrl,
        String techblogUrl,
        int rating,
        Role role,
        Status status,
        long rank
) {
    public static MyProfileResponseDTO from(User user, long rank) {
        return new MyProfileResponseDTO(
                user.getEmail(),
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
}
