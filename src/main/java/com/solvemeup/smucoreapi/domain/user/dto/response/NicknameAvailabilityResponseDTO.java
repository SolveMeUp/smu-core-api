package com.solvemeup.smucoreapi.domain.user.dto.response;

public record NicknameAvailabilityResponseDTO(
        String nickname,
        boolean available
) {
    public static NicknameAvailabilityResponseDTO of(String nickname, boolean available) {
        return new NicknameAvailabilityResponseDTO(nickname, available);
    }
}
