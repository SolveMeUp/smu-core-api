package com.solvemeup.smucoreapi.domain.auth.oauth2.exception;

import lombok.Getter;

/**
 * OAuth2 로그인 요청에서 지원하지 않거나 잘못된 registrationId가 전달된 경우 발생하는 예외.
 */
@Getter
public class InvalidOAuth2RegistrationIdException extends RuntimeException {

    private final String registrationId;

    public InvalidOAuth2RegistrationIdException(String registrationId) {
        super("Invalid OAuth2 registrationId: " + registrationId);
        this.registrationId = registrationId;
    }
}
