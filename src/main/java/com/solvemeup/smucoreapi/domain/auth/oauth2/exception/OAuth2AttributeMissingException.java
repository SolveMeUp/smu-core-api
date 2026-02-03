package com.solvemeup.smucoreapi.domain.auth.oauth2.exception;

import com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider;
import lombok.Getter;

/**
 * OAuth2 공급자 응답에 필수 사용자 속성이 누락된 경우 발생하는 예외.
 */
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
