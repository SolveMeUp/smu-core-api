package com.solvemeup.smucoreapi.domain.user.repository;

import com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider;
import com.solvemeup.smucoreapi.domain.user.entity.Role;
import com.solvemeup.smucoreapi.domain.user.entity.Status;

/**
 * 사용자 랭킹 조회 결과를 표현하는 Projection.
 *
 * <p>랭킹 정보는 전체 사용자 기준 순위이며,
 * 페이징 결과와 무관하게 계산된 값이다.
 *
 * <p>엔티티 {@code User}를 대체하지 않으며,
 * 조회 전용으로만 사용한다.
 */
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
