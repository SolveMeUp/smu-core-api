package com.solvemeup.smucoreapi.domain.user.repository.projection;

public interface UserRankingProjection {
    String getNickname();

    String getProfileImageUrl();

    String getGithubUrl();

    String getTechblogUrl();

    int getRating();

    String getRole();

    String getStatus();

    long getRank();
}
