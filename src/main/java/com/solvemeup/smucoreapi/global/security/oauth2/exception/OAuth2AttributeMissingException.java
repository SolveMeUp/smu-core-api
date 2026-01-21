package com.solvemeup.smucoreapi.global.security.oauth2.exception;

import com.solvemeup.smucoreapi.domain.user.enums.OAuth2Provider;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.OAUTH2_MISSING_ATTRIBUTE;

public class OAuth2AttributeMissingException extends OAuth2AuthenticationException {

    public OAuth2AttributeMissingException(OAuth2Provider provider, String key) {
        super(new OAuth2Error(OAUTH2_MISSING_ATTRIBUTE.name()), provider + " OAuth2 response missing required attribute: " + key);
    }
}
