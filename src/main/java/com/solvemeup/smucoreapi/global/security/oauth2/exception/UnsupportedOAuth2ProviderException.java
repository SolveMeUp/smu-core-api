package com.solvemeup.smucoreapi.global.security.oauth2.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.OAUTH2_UNSUPPORTED_PROVIDER;

public class UnsupportedOAuth2ProviderException extends OAuth2AuthenticationException {

    public UnsupportedOAuth2ProviderException(String registrationId) {
        super(new OAuth2Error(OAUTH2_UNSUPPORTED_PROVIDER.name()), "Unsupported OAuth2 provider: " + registrationId);
    }
}
