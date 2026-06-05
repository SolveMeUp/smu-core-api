package com.solvemeup.smucoreapi.dev.controller;

import com.solvemeup.smucoreapi.domain.auth.oauth2.principal.CustomOAuth2User;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.reader.UserReader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 로컬(local)·개발(dev) 환경 전용 인증 우회 컨트롤러.
 *
 * <p>소셜 로그인 과정을 거치지 않고 지정한 userId로 세션 인증을 바로 생성한다.
 * Postman·curl·프론트 연동 등 빠른 인증이 필요한 테스트 용도다.
 *
 * <p><strong>주의:</strong> 인증을 우회하므로 {@code @Profile({"local", "dev"})}로
 * 운영 환경에서는 절대 로드되지 않는다. 이 프로파일 제약을 반드시 유지할 것.
 */
@Slf4j
@Profile({"local", "dev"})
@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
public class DevController {

    private final UserReader userReader;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> devLogin(@RequestParam Long userId,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) {
        User user = userReader.getUser(userId);

        CustomOAuth2User principal = new CustomOAuth2User(user.getId(), user.getRole());
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        // 세션 고정 방지 및 유저 전환을 위해 기존 세션을 버리고 새로 발급한다.
        HttpSession existingSession = request.getSession(false);
        if (existingSession != null) {
            existingSession.invalidate();
        }

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);

        log.info("[DEV] login success. userId={}", userId);
        return ResponseEntity.ok(Map.of("message", "login success"));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> devLogout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        log.info("[DEV] logout success");
        return ResponseEntity.ok(Map.of("message", "logout success"));
    }
}
