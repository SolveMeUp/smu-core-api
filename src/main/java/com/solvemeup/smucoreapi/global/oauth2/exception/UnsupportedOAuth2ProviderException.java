package com.solvemeup.smucoreapi.global.oauth2.exception;

import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.*;

public class UnsupportedOAuth2ProviderException extends CustomException {

    public UnsupportedOAuth2ProviderException(String registrationId) {
        super(OAUTH2_UNSUPPORTED_PROVIDER, "Unsupported OAuth2 provider: " + registrationId);
    }
}
