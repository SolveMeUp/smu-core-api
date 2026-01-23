package com.solvemeup.smucoreapi.domain.auth.oauth2.response;

import com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider;

public interface OAuth2Response {

    OAuth2Provider getOAuth2Provider();

    String getOAuth2ProviderId();
}
