package com.solvemeup.smucoreapi.global.oauth2.response;

import com.solvemeup.smucoreapi.domain.user.enums.OAuth2Provider;

public interface OAuth2Response {

    OAuth2Provider getOAuth2Provider();

    String getOAuth2ProviderId();

    String getProfileImageUrl();
}
