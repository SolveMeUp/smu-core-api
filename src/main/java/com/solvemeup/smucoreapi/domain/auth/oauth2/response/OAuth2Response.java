package com.solvemeup.smucoreapi.domain.auth.oauth2.response;

import com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider;

/**
 * OAuth2 공급자 응답을 애플리케이션 도메인 모델로 변환하기 위한 인터페이스.
 *
 * <p>각 OAuth2 공급자(Google, GitHub 등)는
 * 이 인터페이스를 구현하여 고유한 응답 구조를 캡슐화한다.
 */
public interface OAuth2Response {

    /**
     * OAuth2 공급자 타입을 반환한다.
     */
    OAuth2Provider getOAuth2Provider();

    /**
     * OAuth2 공급자가 제공하는 사용자 고유 식별자를 반환한다.
     */
    String getOAuth2ProviderId();
}
