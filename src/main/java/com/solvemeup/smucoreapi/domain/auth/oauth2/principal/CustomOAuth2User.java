package com.solvemeup.smucoreapi.domain.auth.oauth2.principal;

import com.solvemeup.smucoreapi.domain.user.entity.UserRole;
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
 * <p>세션 기반 인증을 전제로 하며, Redis 등 외부 세션 저장소에
 * 직렬화되어 저장되므로 {@link Serializable}을 구현한다.
 */
public record CustomOAuth2User(Long userId, UserRole role) implements OAuth2User, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * {@link OAuth2User} 계약 충족용 속성 맵.
     *
     * <p>공급자 원본 응답이 아니라 내부 인증 컨텍스트(userId·role)를 담는다.
     */
    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
                "userId", userId,
                "role", role
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
