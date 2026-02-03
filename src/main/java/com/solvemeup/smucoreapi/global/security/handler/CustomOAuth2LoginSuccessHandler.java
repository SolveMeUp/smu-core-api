package com.solvemeup.smucoreapi.global.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * OAuth2 로그인 성공 시 처리 핸들러.
 *
 * <p>OAuth2 인증이 성공하면 클라이언트 콜백 URL로 리다이렉트하여
 * 이후 로그인 후 처리를 진행한다.
 */
@Slf4j
@Component
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.debug("OAuth2 login success");

        response.sendRedirect("/auth/callback");
    }
}
