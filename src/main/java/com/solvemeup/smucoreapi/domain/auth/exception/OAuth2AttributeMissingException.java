package com.solvemeup.smucoreapi.domain.auth.exception;

import com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.OAUTH2_MISSING_ATTRIBUTE;

/**
 * OAuth2 공급자 응답에 필수 사용자 속성이 누락된 경우 발생하는 예외.
 */
public class OAuth2AttributeMissingException extends OAuth2LoginException {

    public OAuth2AttributeMissingException(OAuth2Provider oauth2Provider, String attributeKey) {
        super(OAUTH2_MISSING_ATTRIBUTE, oauth2Provider + " OAuth2 response missing required attribute: " + attributeKey);
    }
}
