package com.solvemeup.smucoreapi.domain.auth.oauth2.exception;

import lombok.Getter;

/**
 * 애플리케이션에서 지원하지 않는 OAuth2 공급자가 요청된 경우 발생하는 예외.
 */
@Getter
public class UnsupportedOAuth2ProviderException extends RuntimeException {

    private final String registrationId;

    public UnsupportedOAuth2ProviderException(String registrationId) {
        super("Unsupported OAuth2 provider: " + registrationId);
        this.registrationId = registrationId;
    }
}
