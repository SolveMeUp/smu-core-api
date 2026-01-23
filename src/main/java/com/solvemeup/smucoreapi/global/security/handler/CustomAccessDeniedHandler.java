package com.solvemeup.smucoreapi.global.security.handler;

import com.solvemeup.smucoreapi.global.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(AUTH_FORBIDDEN.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse body = ErrorResponse.of(request.getRequestURI(), AUTH_FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
