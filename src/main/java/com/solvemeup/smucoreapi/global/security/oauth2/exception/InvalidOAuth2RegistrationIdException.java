package com.solvemeup.smucoreapi.global.security.oauth2.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.OAUTH2_INVALID_REGISTRATION_ID;

public class InvalidOAuth2RegistrationIdException extends OAuth2AuthenticationException {

    public InvalidOAuth2RegistrationIdException(String registrationId) {
        super(new OAuth2Error(OAUTH2_INVALID_REGISTRATION_ID.name()), "Invalid OAuth2 registrationId: " + registrationId);
    }
}
