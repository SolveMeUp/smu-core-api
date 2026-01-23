package com.solvemeup.smucoreapi.domain.user.repository;

import com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider;
import com.solvemeup.smucoreapi.domain.user.entity.Role;
import com.solvemeup.smucoreapi.domain.user.entity.Status;

public interface UserRankingProjection {

    Long getId();

    OAuth2Provider getOauth2Provider();

    String getNickname();

    String getProfileImageUrl();

    String getGithubUrl();

    String getTechblogUrl();

    int getRating();

    Role getRole();

    Status getStatus();

    long getRank();
}
