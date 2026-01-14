package com.solvemeup.smucoreapi.global.oauth2.dto;

import com.solvemeup.smucoreapi.domain.user.enums.OAuth2Provider;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Map;

import static com.solvemeup.smucoreapi.domain.user.enums.OAuth2Provider.*;

@RequiredArgsConstructor
@ToString
public class GithubResponseDTO {

    private final Map<String, Object> attributes;

    public OAuth2Provider getOAuth2Provider() {
        return GITHUB;
    }

    public String getOAuth2ProviderId() {
        return attributes.get("id").toString();
    }

    public String getNickname() {
        return attributes.get("login").toString();
    }

    public String getProfileImageUrl() {
        return attributes.get("avatar_url").toString();
    }
}
