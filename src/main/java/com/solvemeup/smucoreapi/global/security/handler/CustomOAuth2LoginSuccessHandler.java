package com.solvemeup.smucoreapi.global.security.handler;

import com.solvemeup.smucoreapi.domain.auth.oauth2.principal.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${app.oauth2.login-redirect-uri}")
    private String defaultRedirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String redirectUri = request.getParameter("redirect_uri");
        if (redirectUri == null || redirectUri.isBlank()) {
            redirectUri = defaultRedirectUri;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomOAuth2User customOAuth2User)) {
            response.sendRedirect(redirectUri);
            return;
        }

        String finalRedirectUri = UriComponentsBuilder
                .fromUriString(redirectUri)
                .queryParam("auth", "success")
                .queryParam("event", customOAuth2User.getLoginEvent().name())
                .build()
                .encode()
                .toUriString();

        response.sendRedirect(finalRedirectUri);
    }
}
