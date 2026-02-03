package com.solvemeup.smucoreapi.global.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * OAuth2 로그인 실패 시 처리 핸들러.
 *
 * <p>OAuth2 인증 실패가 발생하면 클라이언트 콜백 URL로
 * 실패 원인을 포함하여 리다이렉트한다.
 */
@Slf4j
@Component
public class CustomOAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.debug("OAuth2 login failed: {}", exception.getClass().getSimpleName());

        response.sendRedirect("/auth/callback?error=oauth_failed");
    }
}
