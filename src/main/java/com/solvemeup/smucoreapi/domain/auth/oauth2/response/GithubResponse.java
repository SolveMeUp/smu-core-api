package com.solvemeup.smucoreapi.domain.auth.oauth2.response;

import com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider;
import com.solvemeup.smucoreapi.domain.auth.oauth2.exception.OAuth2AttributeMissingException;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Map;

import static com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider.GITHUB;

@RequiredArgsConstructor
@ToString
public final class GithubResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    @Override
    public OAuth2Provider getOAuth2Provider() {
        return GITHUB;
    }

    @Override
    public String getOAuth2ProviderId() {
        return requiredString("id");
    }

    private String requiredString(String key) {
        Object value = attributes.get(key);
        if (value == null) {
            throw new OAuth2AttributeMissingException(GITHUB, key);
        }
        return value.toString();
    }
}
