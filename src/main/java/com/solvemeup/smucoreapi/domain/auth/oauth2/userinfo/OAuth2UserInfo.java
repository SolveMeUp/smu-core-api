package com.solvemeup.smucoreapi.domain.auth.oauth2.userinfo;

import com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider;

/**
 * OAuth2 공급자가 내려준 사용자 정보를 공급자와 무관하게 다루기 위한 추상화.
 *
 * <p>공급자마다 제각각인 응답(attributes)에서 인증에 필요한 식별 정보만
 * 일관된 형태로 노출한다. 공급자를 추가할 때는 이 인터페이스를 구현한다.
 */
public interface OAuth2UserInfo {

    /**
     * 이 사용자 정보를 제공한 OAuth2 공급자를 반환한다.
     */
    OAuth2Provider getOAuth2Provider();

    /**
     * 공급자 내에서 사용자를 식별하는 고유 ID를 반환한다.
     */
    String getOAuth2ProviderId();
}
