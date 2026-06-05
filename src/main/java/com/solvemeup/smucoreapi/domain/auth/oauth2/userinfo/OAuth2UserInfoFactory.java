package com.solvemeup.smucoreapi.domain.auth.oauth2.userinfo;

import com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider;
import com.solvemeup.smucoreapi.domain.auth.exception.UnsupportedOAuth2ProviderException;

import java.util.Map;

/**
 * registrationId에 맞는 {@link OAuth2UserInfo} 구현체를 생성하는 팩토리.
 */
public final class OAuth2UserInfoFactory {

    private OAuth2UserInfoFactory() {
        throw new AssertionError("OAuth2UserInfoFactory is a Utility class");
    }

    /**
     * registrationId와 attributes로 해당 공급자의 {@link OAuth2UserInfo}를 생성한다.
     *
     * @throws UnsupportedOAuth2ProviderException registrationId가 알 수 없는 값이거나, 아직 지원하지 않는 공급자일 경우
     */
    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        OAuth2Provider oauth2Provider = parseProvider(registrationId);

        return switch (oauth2Provider) {
            case GITHUB -> new GithubOAuth2UserInfo(attributes);
            default -> throw new UnsupportedOAuth2ProviderException(registrationId);
        };
    }

    /**
     * registrationId를 {@link OAuth2Provider}로 변환한다.
     *
     * @throws UnsupportedOAuth2ProviderException registrationId에 대응하는 공급자가 없을 경우
     */
    private static OAuth2Provider parseProvider(String registrationId) {
        try {
            return OAuth2Provider.valueOf(registrationId.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedOAuth2ProviderException(registrationId);
        }
    }
}
