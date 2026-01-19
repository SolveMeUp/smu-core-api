package com.solvemeup.smucoreapi.global.auth;

import com.solvemeup.smucoreapi.domain.user.enums.Role;

public record MeResponseDTO(Long userId, Role role) {
}
