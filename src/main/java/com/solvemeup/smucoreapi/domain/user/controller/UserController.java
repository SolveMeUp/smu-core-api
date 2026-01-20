package com.solvemeup.smucoreapi.domain.user.controller;

import com.solvemeup.smucoreapi.domain.user.dto.response.MyProfileResponseDTO;
import com.solvemeup.smucoreapi.domain.user.dto.response.NicknameAvailabilityResponseDTO;
import com.solvemeup.smucoreapi.domain.user.dto.response.UserProfileResponseDTO;
import com.solvemeup.smucoreapi.domain.user.service.UserService;
import com.solvemeup.smucoreapi.global.auth.AuthUserId;
import com.solvemeup.smucoreapi.global.dto.PageResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<MyProfileResponseDTO> getMyProfile(@AuthUserId Long userId) {
        return ResponseEntity.ok(userService.getMyProfile(userId));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(@AuthUserId Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDTO> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/ranking")
    public ResponseEntity<PageResponse<UserProfileResponseDTO>> getRanking(@RequestParam(defaultValue = "0") @Min(0) @Max(100000) int page,
                                                                           @RequestParam(defaultValue = "100") @Min(10) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(PageResponse.from(userService.getRanking(pageable)));
    }

    @GetMapping("/nickname/availability")
    public ResponseEntity<NicknameAvailabilityResponseDTO> checkNicknameAvailability(@RequestParam @Size(min = 4, max = 20) String nickname) {
        boolean available = userService.checkNicknameAvailability(nickname);
        return ResponseEntity.ok(NicknameAvailabilityResponseDTO.of(nickname, available));
    }
}
