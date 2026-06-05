package com.solvemeup.smucoreapi.domain.auth.dto.response;

import com.solvemeup.smucoreapi.domain.user.entity.UserRole;

public record MyAuthResponse(Long userId, UserRole role) {
}
