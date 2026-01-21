package com.solvemeup.smucoreapi.domain.auth.dto.response;

import com.solvemeup.smucoreapi.domain.user.enums.Role;

public record MyAuthResponse(Long userId, Role role) {
}
