package com.solvemeup.smucoreapi.global.security.handler;

import com.solvemeup.smucoreapi.global.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.*;

/**
 * 인가 실패(권한 부족) 시 공통 처리 핸들러.
 *
 * <p>인증은 되었으나 접근 권한이 없는 리소스에 접근할 경우
 * HTTP 403 응답과 표준 에러 바디를 JSON 형식으로 반환한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.debug("Access denied: {}", request.getRequestURI());

        response.setStatus(AUTH_FORBIDDEN.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorResponse body = ErrorResponse.of(request.getRequestURI(), AUTH_FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
