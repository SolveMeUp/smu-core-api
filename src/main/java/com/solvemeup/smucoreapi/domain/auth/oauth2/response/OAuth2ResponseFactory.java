package com.solvemeup.smucoreapi.domain.auth.oauth2.response;

import com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider;
import com.solvemeup.smucoreapi.domain.auth.oauth2.exception.InvalidOAuth2RegistrationIdException;
import com.solvemeup.smucoreapi.domain.auth.oauth2.exception.UnsupportedOAuth2ProviderException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * OAuth2 공급자별 {@link OAuth2Response} 구현체를 생성하는 팩토리.
 *
 * <p>Spring Security OAuth2 로그인 과정에서 전달되는
 * registrationId와 attributes를 기반으로
 * 적절한 OAuth2Response 구현체를 반환한다.
 */
@Slf4j
public final class OAuth2ResponseFactory {

    private OAuth2ResponseFactory() {
        throw new AssertionError("OAuth2ResponseFactory is a Utility class");
    }

    /**
     * OAuth2 공급자 식별자와 사용자 속성을 기반으로 적절한 OAuth2Response 구현체를 생성한다.
     *
     * @throws UnsupportedOAuth2ProviderException 지원하지 않는 공급자일 경우
     */
    public static OAuth2Response of(String registrationId, Map<String, Object> attributes) {
        OAuth2Provider oauth2Provider = parseProvider(registrationId);

        return switch (oauth2Provider) {
            case GITHUB -> new GithubResponse(attributes);
            default -> throw new UnsupportedOAuth2ProviderException(registrationId);
        };
    }

    /**
     * registrationId 문자열을 OAuth2Provider enum으로 변환한다.
     *
     * @throws InvalidOAuth2RegistrationIdException registrationId가 유효하지 않을 경우
     */
    private static OAuth2Provider parseProvider(String registrationId) {
        try {
            log.debug("Parsing OAuth2Response for provider={}", registrationId);

            return OAuth2Provider.valueOf(registrationId.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidOAuth2RegistrationIdException(registrationId);
        }
    }
}
