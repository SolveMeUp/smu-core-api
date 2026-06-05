package com.solvemeup.smucoreapi.domain.auth.exception;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.OAUTH2_UNSUPPORTED_PROVIDER;

/**
 * 애플리케이션에서 지원하지 않는 OAuth2 공급자가 요청된 경우 발생하는 예외.
 */
public class UnsupportedOAuth2ProviderException extends OAuth2LoginException {

    public UnsupportedOAuth2ProviderException(String registrationId) {
        super(OAUTH2_UNSUPPORTED_PROVIDER, "Unsupported OAuth2 provider: " + registrationId);
    }
}
