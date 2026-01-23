package com.solvemeup.smucoreapi.global.security.handler;

import com.solvemeup.smucoreapi.global.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(AUTH_UNAUTHORIZED.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse body = ErrorResponse.of(request.getRequestURI(), AUTH_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
