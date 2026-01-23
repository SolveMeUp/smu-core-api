package com.solvemeup.smucoreapi.global.security.handler;

import com.solvemeup.smucoreapi.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class CustomOAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    @Value("${app.oauth2.login-redirect-uri}")
    private String loginRedirectUri;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String errorCode = ErrorCode.OAUTH2_LOGIN_FAILED.getCode();
        if (exception instanceof OAuth2AuthenticationException oAuth2Ex) {
            errorCode = oAuth2Ex.getError().getErrorCode();
        }

        String redirectUri = UriComponentsBuilder
                .fromUriString(loginRedirectUri)
                .queryParam("auth", "fail")
                .queryParam("error", errorCode)
                .build()
                .encode()
                .toUriString();

        response.sendRedirect(redirectUri);
    }
}
