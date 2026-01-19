package com.solvemeup.smucoreapi.global.oauth2.exception;

import com.solvemeup.smucoreapi.domain.user.enums.OAuth2Provider;
import com.solvemeup.smucoreapi.global.exception.CustomException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.*;

public class OAuth2AttributeMissingException extends CustomException {

    public OAuth2AttributeMissingException(OAuth2Provider provider, String key) {
        super(OAUTH2_MISSING_ATTRIBUTE, provider + " OAuth2 response missing required attribute: " + key);
    }
}
