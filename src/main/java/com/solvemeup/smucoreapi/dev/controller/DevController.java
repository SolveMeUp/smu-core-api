package com.solvemeup.smucoreapi.dev.controller;

import com.solvemeup.smucoreapi.domain.auth.oauth2.principal.CustomOAuth2User;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.reader.UserReader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Dev", description = "로컬·개발 전용 인증 우회 API (운영 미로드)")
@Slf4j
@Profile({"local", "dev"})
@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
public class DevController {

    private final UserReader userReader;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @Operation(
            summary = "개발용 로그인 (인증 우회)",
            description = """
                    소셜 로그인(OAuth2)을 거치지 않고 지정한 userId로 즉시 세션을 발급한다.

                    동작:
                    - 호출하면 응답에 SESSION 쿠키가 실려 이 브라우저가 해당 유저로 로그인된다.
                    - 이후 인증이 필요한 API(예: /api/users/me)를 그대로 호출하면 쿠키가 자동으로 실려 인증된다.
                    - 기존 세션이 있으면 폐기하고 새로 발급하므로, 다른 userId로 다시 호출하면 유저가 전환된다.

                    Swagger에서 인증 API를 테스트하려면 먼저 이 API를 Execute 한 뒤 원하는 API를 호출하면 된다.
                    local·dev 프로파일에서만 노출되며 운영에는 로드되지 않는다.""")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> devLogin(
            @Parameter(description = "세션을 발급할 대상 유저의 ID", example = "1")
            @RequestParam Long userId,
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

    @Operation(
            summary = "개발용 로그아웃",
            description = "현재 세션을 무효화한다. 이후 인증 API 호출은 다시 미인증 상태가 된다.")
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
