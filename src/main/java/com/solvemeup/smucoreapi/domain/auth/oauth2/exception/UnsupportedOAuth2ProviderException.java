package com.solvemeup.smucoreapi.domain.auth.oauth2.exception;

import lombok.Getter;

@Getter
public class UnsupportedOAuth2ProviderException extends RuntimeException {

    private final String registrationId;

    public UnsupportedOAuth2ProviderException(String registrationId) {
        super("Unsupported OAuth2 provider: " + registrationId);
        this.registrationId = registrationId;
    }
}
