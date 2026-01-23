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

@Getter
@RequiredArgsConstructor
public final class CustomOAuth2User implements OAuth2User, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long userId;
    private final Role role;
    private final LoginEvent loginEvent;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of("userId", userId, "role", role, "loginEvent", loginEvent);
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
