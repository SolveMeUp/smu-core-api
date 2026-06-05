package com.solvemeup.smucoreapi.global.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * OAuth2 로그인 실패 시 처리 핸들러.
 *
 * <p>OAuth2 인증 실패가 발생하면 클라이언트 콜백 URL로
 * 실패 원인(에러 코드)을 포함하여 리다이렉트한다.
 *
 * <p>{@code CustomOAuth2UserService}에서 래핑한 OAuth2 인증 예외의 에러 코드를 보존하여,
 * 프론트엔드가 실패 사유별로 분기할 수 있도록 한다.
 */
@Slf4j
@Component
public class CustomOAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    private static final String CALLBACK_URI = "/auth/callback";
    private static final String DEFAULT_ERROR = "oauth_failed";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String error = resolveError(exception);

        log.debug("OAuth2 login failed: type={}, error={}", exception.getClass().getSimpleName(), error);

        String redirectUrl = UriComponentsBuilder.fromUriString(CALLBACK_URI)
                .queryParam("error", error)
                .build()
                .encode()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }

    private String resolveError(AuthenticationException exception) {
        if (exception instanceof OAuth2AuthenticationException oauth2Exception) {
            String code = oauth2Exception.getError().getErrorCode();
            if (StringUtils.hasText(code)) {
                return code;
            }
        }
        return DEFAULT_ERROR;
    }
}
