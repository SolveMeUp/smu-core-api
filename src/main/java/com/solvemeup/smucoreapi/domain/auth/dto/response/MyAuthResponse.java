package com.solvemeup.smucoreapi.domain.auth.dto.response;

import com.solvemeup.smucoreapi.domain.user.entity.Role;

public record MyAuthResponse(Long userId, Role role) {
}
