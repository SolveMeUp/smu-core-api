package com.solvemeup.smucoreapi.domain.auth.oauth2.exception;

import lombok.Getter;

@Getter
public class InvalidOAuth2RegistrationIdException extends RuntimeException {

    private final String registrationId;

    public InvalidOAuth2RegistrationIdException(String registrationId) {
        super("Invalid OAuth2 registrationId: " + registrationId);
        this.registrationId = registrationId;
    }
}
