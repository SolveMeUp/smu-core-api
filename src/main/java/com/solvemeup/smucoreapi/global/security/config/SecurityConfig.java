package com.solvemeup.smucoreapi.global.security.config;

import com.solvemeup.smucoreapi.domain.auth.oauth2.service.CustomOAuth2UserService;
import com.solvemeup.smucoreapi.global.security.handler.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${app.cors.allowed-origins}")
    private List<String> corsOrigins;

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;
    private final CustomOAuth2LoginFailureHandler customOAuth2LoginFailureHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {

        http.csrf(AbstractHttpConfigurer::disable);

        http.cors(Customizer.withDefaults());

        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
        );

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index.html", "/favicon.ico").permitAll()
                .requestMatchers("/oauth2/**", "/login/**", "/error").permitAll()
                .requestMatchers("/api/dev/**").permitAll()

                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                .requestMatchers("/api/auth/logout").permitAll()
                .requestMatchers("/api/auth/**").authenticated()

                .requestMatchers("/api/users/me/**").authenticated()
                .requestMatchers("/api/users/**").permitAll()

                .requestMatchers("/api/posts/**").permitAll()

                .anyRequest().authenticated()
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(corsOrigins);

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
