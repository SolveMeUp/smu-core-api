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

/**
 * 인증되지 않은 요청에 대한 공통 처리 EntryPoint.
 *
 * <p>인증이 필요한 API에 대해 인증 정보가 없을 경우
 * HTTP 401 응답과 표준 에러 바디를 JSON 형식으로 반환한다.
 */
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
