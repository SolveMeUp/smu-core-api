package com.solvemeup.smucoreapi.domain.user.controller;

import com.solvemeup.smucoreapi.domain.user.dto.response.MyProfileResponseDTO;
import com.solvemeup.smucoreapi.domain.user.dto.response.UserProfileResponseDTO;
import com.solvemeup.smucoreapi.domain.user.service.UserService;
import com.solvemeup.smucoreapi.global.auth.AuthUserId;
import com.solvemeup.smucoreapi.global.dto.PageResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @GetMapping("/ranking")
    public ResponseEntity<PageResponse<UserProfileResponseDTO>> getRanking(@RequestParam(defaultValue = "0") @Min(0) @Max(100000) int page,
                                                                           @RequestParam(defaultValue = "100") @Min(10) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(PageResponse.from(userService.getRanking(pageable)));
    }

    @GetMapping("/profile/{nickname}")
    public ResponseEntity<UserProfileResponseDTO> getUser(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.getUser(nickname));
    }
}
