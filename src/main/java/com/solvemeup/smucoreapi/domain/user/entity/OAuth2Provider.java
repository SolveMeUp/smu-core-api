package com.solvemeup.smucoreapi.domain.user.entity;

/**
 * 소셜 로그인 제공자.
 *
 * <p>사용자가 어떤 OAuth2 제공자를 통해 인증되었는지를 나타낸다.
 */
public enum OAuth2Provider {

    /**
     * Google OAuth2 로그인
     */
    GOOGLE,

    /**
     * GitHub OAuth2 로그인
     */
    GITHUB
}
