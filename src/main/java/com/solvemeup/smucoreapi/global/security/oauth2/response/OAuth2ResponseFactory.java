package com.solvemeup.smucoreapi.global.security.oauth2.response;

import com.solvemeup.smucoreapi.domain.user.enums.OAuth2Provider;
import com.solvemeup.smucoreapi.global.security.oauth2.exception.InvalidOAuth2RegistrationIdException;
import com.solvemeup.smucoreapi.global.security.oauth2.exception.UnsupportedOAuth2ProviderException;

import java.util.Map;

public final class OAuth2ResponseFactory {

    private OAuth2ResponseFactory() {
        throw new AssertionError("OAuth2ResponseFactory is a Utility class");
    }

    public static OAuth2Response of(String registrationId, Map<String, Object> attributes) {
        OAuth2Provider provider = parseProvider(registrationId);

        return switch (provider) {
            case GITHUB -> new GithubResponse(attributes);
            default -> throw new UnsupportedOAuth2ProviderException(registrationId);
        };
    }

    private static OAuth2Provider parseProvider(String registrationId) {
        try {
            return OAuth2Provider.valueOf(registrationId.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidOAuth2RegistrationIdException(registrationId);
        }
    }
}
