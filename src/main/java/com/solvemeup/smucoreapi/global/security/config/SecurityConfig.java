package com.solvemeup.smucoreapi.global.security.config;

import com.solvemeup.smucoreapi.global.security.handler.CustomAccessDeniedHandler;
import com.solvemeup.smucoreapi.global.security.handler.CustomAuthenticationEntryPoint;
import com.solvemeup.smucoreapi.global.security.handler.CustomOAuth2FailureHandler;
import com.solvemeup.smucoreapi.global.security.oauth2.service.CustomOAuth2UserService;
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

    @Value("${app.oauth2.login-success-redirect-uri}")
    private String loginSuccessRedirectUri;

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomOAuth2FailureHandler failureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {

        http.csrf(AbstractHttpConfigurer::disable);

        http.cors(Customizer.withDefaults());

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index.html").permitAll()
                .requestMatchers("/oauth2/**", "/login/**", "/error").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                .requestMatchers("/api/users/me").authenticated()
                .requestMatchers("/api/users/me/**").authenticated()
                .requestMatchers("/api/users/**").permitAll()

                .requestMatchers("/api/posts/**").permitAll()

                .anyRequest().authenticated()
        );

        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        );

        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService)
                )
                .defaultSuccessUrl(loginSuccessRedirectUri, true)
                .failureHandler(failureHandler)
        );

        http.logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .invalidateHttpSession(true)
                .deleteCookies("SESSION")
                .logoutSuccessHandler((request, response, authentication) -> response.setStatus(200))
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
