package com.solvemeup.smucoreapi.domain.user.dto.response;

import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.enums.Role;
import com.solvemeup.smucoreapi.domain.user.enums.Status;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserProfileResponseDTO {

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
