package com.solvemeup.smucoreapi.domain.user.repository.projection;

import com.solvemeup.smucoreapi.domain.user.enums.OAuth2Provider;

public interface UserRankingProjection {

    Long getId();

    OAuth2Provider getOAuth2Provider();

    String getNickname();

    String getProfileImageUrl();

    String getGithubUrl();

    String getTechblogUrl();

    int getRating();

    String getRole();

    String getStatus();

    long getRank();
}
