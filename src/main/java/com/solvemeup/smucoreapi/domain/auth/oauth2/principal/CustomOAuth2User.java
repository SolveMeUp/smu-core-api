package com.solvemeup.smucoreapi.domain.auth.oauth2.principal;

import com.solvemeup.smucoreapi.domain.user.entity.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * OAuth2 인증 이후 SecurityContext에 저장되는 사용자 Principal.
 *
 * <p>애플리케이션 내부 사용자 식별을 위해
 * userId, role, 로그인 이벤트 정보를 포함한다.
 *
 * <p>세션 기반 인증을 전제로 하며,
 * Redis 등 외부 세션 저장소를 고려해 직렬화를 지원한다.
 */
@Getter
@RequiredArgsConstructor
public final class CustomOAuth2User implements OAuth2User, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long userId;
    private final Role role;
    private final LoginEvent loginEvent;

    /**
     * OAuth2User 인터페이스 요구사항을 충족하기 위한 최소한의 속성 맵.
     *
     * <p>OAuth 공급자 원본 응답이 아닌,
     * 애플리케이션 내부 인증 컨텍스트 전달 용도로 사용된다.
     */
    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
                "userId", userId,
                "role", role,
                "loginEvent", loginEvent
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getName() {
        return userId.toString();
    }
}
