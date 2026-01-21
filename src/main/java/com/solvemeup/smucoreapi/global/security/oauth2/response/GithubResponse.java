package com.solvemeup.smucoreapi.global.security.oauth2.response;

import com.solvemeup.smucoreapi.domain.user.enums.OAuth2Provider;
import com.solvemeup.smucoreapi.global.security.oauth2.exception.OAuth2AttributeMissingException;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Map;

import static com.solvemeup.smucoreapi.domain.user.enums.OAuth2Provider.GITHUB;

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

    @Override
    public String getProfileImageUrl() {
        return requiredString("avatar_url");
    }

    private String requiredString(String key) {
        Object value = attributes.get(key);
        if (value == null) {
            throw new OAuth2AttributeMissingException(GITHUB, key);
        }
        return value.toString();
    }
}
