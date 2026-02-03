package com.solvemeup.smucoreapi.dev.controller;

import com.solvemeup.smucoreapi.domain.auth.oauth2.principal.CustomOAuth2User;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.reader.UserReader;
import com.solvemeup.smucoreapi.global.exception.ErrorCode;
import com.solvemeup.smucoreapi.global.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.solvemeup.smucoreapi.domain.auth.oauth2.principal.LoginEvent.*;

/**
 * 로컬(local) 및 개발(dev) 환경에서만 사용되는 인증 우회 컨트롤러.
 *
 * <p>OAuth2 로그인 과정을 거치지 않고,
 * 지정한 사용자 ID에 대해 세션 기반 인증을 직접 생성한다.
 *
 * <p>주 용도:
 * <ul>
 *   <li>Postman, curl 등을 이용한 API 테스트</li>
 *   <li>프론트엔드/백엔드 연동 개발 시 빠른 인증 처리</li>
 *   <li>비동기 로직, 메시징, 권한 로직 검증</li>
 * </ul>
 *
 * <p>이 컨트롤러는 {@code local}, {@code dev} 프로파일에서만 활성화되며,
 * 운영(production) 환경에서는 절대 로드되지 않는다.
 *
 * <p><strong>주의:</strong>
 * 이 컨트롤러는 인증 보안을 우회하므로
 * 운영 환경에 포함되지 않도록 프로파일 설정을 반드시 유지해야 한다.
 */
@Slf4j
@Profile({"local", "dev"})
@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
public class DevController {

    private final UserReader userReader;

    @PostMapping("/login")
    public ResponseEntity<?> devLogin(@RequestParam Long userId, HttpServletRequest request) {
        HttpSession existingSession = request.getSession(false);
        if (existingSession != null) {
            Object ctx = existingSession.getAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY
            );

            if (ctx instanceof SecurityContext securityContext) {
                Authentication authentication = securityContext.getAuthentication();

                if (authentication != null
                        && authentication.isAuthenticated()
                        && authentication.getPrincipal() instanceof CustomOAuth2User currentUser) {

                    Long currentUserId = currentUser.getUserId();

                    if (currentUserId.equals(userId)) {
                        log.info("[DEV] already logged in as same user. userId={}", userId);
                        return ResponseEntity.ok(
                                Map.of("message", "already logged in")
                        );
                    }

                    log.info("[DEV] switch login {} -> {}", currentUserId, userId);
                    existingSession.invalidate();
                }
            }
        }

        User user = userReader.getUser(userId);
        if (user.isBlocked()) {
            return ResponseEntity
                    .status(ErrorCode.AUTH_BLOCKED_USER.getStatus())
                    .body(ErrorResponse.of(request.getRequestURI(), ErrorCode.AUTH_BLOCKED_USER));
        }

        CustomOAuth2User principal =
                new CustomOAuth2User(user.getId(), user.getRole(), NONE);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities()
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        HttpSession newSession = request.getSession(true);
        newSession.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context
        );

        log.info("[DEV] login success. userId={}", userId);
        return ResponseEntity.ok(
                Map.of("message", "login success")
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> devLogout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);

        if (session == null) {
            log.info("[DEV] logout requested but no active session");
            return ResponseEntity.ok(
                    Map.of("message", "already logged out")
            );
        }

        session.invalidate();
        log.info("[DEV] logout success");

        return ResponseEntity.ok(
                Map.of("message", "logout success")
        );
    }
}
