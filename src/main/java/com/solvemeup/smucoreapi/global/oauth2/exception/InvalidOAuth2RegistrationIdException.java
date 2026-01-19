package com.solvemeup.smucoreapi.global.oauth2.exception;

import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.*;

public class InvalidOAuth2RegistrationIdException extends CustomException {

    public InvalidOAuth2RegistrationIdException(String registrationId) {
        super(OAUTH2_INVALID_REGISTRATION_ID, "Invalid OAuth2 registrationId: " + registrationId);
    }
}
