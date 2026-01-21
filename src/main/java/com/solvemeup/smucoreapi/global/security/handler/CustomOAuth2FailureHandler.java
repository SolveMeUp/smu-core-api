package com.solvemeup.smucoreapi.global.security.handler;

import com.solvemeup.smucoreapi.global.exception.ErrorCode;
import com.solvemeup.smucoreapi.global.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2FailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        ErrorCode errorCode = resolveErrorCode(exception);

        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse body = ErrorResponse.of(request.getRequestURI(), errorCode);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    private ErrorCode resolveErrorCode(AuthenticationException exception) {
        if (!(exception instanceof OAuth2AuthenticationException oAuth2Ex)) {
            return ErrorCode.OAUTH2_LOGIN_FAILED;
        }

        String errorName = oAuth2Ex.getError().getErrorCode();

        try {
            return ErrorCode.valueOf(errorName);
        } catch (IllegalArgumentException ignored) {
            return ErrorCode.OAUTH2_LOGIN_FAILED;
        }
    }
}
