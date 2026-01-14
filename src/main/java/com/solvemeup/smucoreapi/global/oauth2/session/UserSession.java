package com.solvemeup.smucoreapi.global.oauth2.session;

import com.solvemeup.smucoreapi.domain.user.enums.Role;

import java.io.Serializable;

public record UserSession(Long id, Role role) implements Serializable {
}
