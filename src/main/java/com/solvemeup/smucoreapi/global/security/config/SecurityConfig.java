package com.solvemeup.smucoreapi.global.security.config;

import com.solvemeup.smucoreapi.domain.auth.oauth2.service.CustomOAuth2UserService;
import com.solvemeup.smucoreapi.global.security.handler.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 애플리케이션 전역 보안 설정.
 *
 * <p>세션 기반 인증을 사용하며 OAuth2 로그인을 통해 사용자를 인증한다.
 *
 * <p>주요 정책:
 * <ul>
 *   <li>OAuth2 로그인 엔드포인트 및 공개 API는 인증 없이 접근 가능</li>
 *   <li>관리자 API(/api/admin/**)는 ADMIN 권한 필요</li>
 *   <li>인증/인가 실패 시 JSON 에러 응답을 반환</li>
 *   <li>CSRF: 운영(prod)은 SPA 쿠키 토큰 방식으로 활성, 로컬/개발은 테스트 편의를 위해 비활성</li>
 * </ul>
 *
 * <p>로그인 성공/실패, 로그아웃 성공 시의 처리는
 * 커스텀 핸들러를 통해 제어한다.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;
    private final CustomOAuth2LoginFailureHandler customOAuth2LoginFailureHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Value("${app.security.csrf.enabled:true}")
    private boolean csrfEnabled;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> {
            if (csrfEnabled) {
                csrf.spa();
            } else {
                csrf.disable();
            }
        });

        http.authorizeHttpRequests(auth -> auth
                // 인증/로그인 관련
                .requestMatchers("/oauth2/**", "/login/**", "/error").permitAll()
                .requestMatchers("/api/auth/logout").permitAll()
                .requestMatchers("/api/auth/**").authenticated()

                // API 문서 (Swagger UI / OpenAPI 스펙)
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()

                // 모니터링
                .requestMatchers("/actuator/**").permitAll()

                // 개발용
                .requestMatchers("/api/dev/**").permitAll()

                // 관리자
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // 유저 도메인
                .requestMatchers("/api/users/me/**").authenticated()
                .requestMatchers("/api/users/**").permitAll()

                // 커뮤니티 도메인
                .requestMatchers("/api/posts/**").permitAll()

                .anyRequest().authenticated()
        );

        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
        );

        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService)
                )
                .successHandler(customOAuth2LoginSuccessHandler)
                .failureHandler(customOAuth2LoginFailureHandler)
        );

        http.logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .invalidateHttpSession(true)
                .deleteCookies("SESSION")
                .logoutSuccessHandler(customLogoutSuccessHandler)
        );

        return http.build();
    }
}
