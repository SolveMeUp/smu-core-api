package com.solvemeup.smucoreapi.domain.auth.oauth2.exception;

import com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider;
import lombok.Getter;

@Getter
public class OAuth2AttributeMissingException extends RuntimeException {

    private final OAuth2Provider oauth2Provider;
    private final String attributeKey;

    public OAuth2AttributeMissingException(OAuth2Provider oauth2Provider, String attributeKey) {
        super(oauth2Provider + " OAuth2 response missing required attribute: " + attributeKey);
        this.oauth2Provider = oauth2Provider;
        this.attributeKey = attributeKey;
    }
}
