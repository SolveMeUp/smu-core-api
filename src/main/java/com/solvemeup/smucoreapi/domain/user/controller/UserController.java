package com.solvemeup.smucoreapi.domain.user.controller;

import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyEmailRequest;
import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyGithubUrlRequest;
import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyNicknameRequest;
import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyTechblogUrlRequest;
import com.solvemeup.smucoreapi.domain.user.dto.response.*;
import com.solvemeup.smucoreapi.domain.user.service.UserService;
import com.solvemeup.smucoreapi.global.dto.response.PageResponse;
import com.solvemeup.smucoreapi.global.security.annotation.AuthUserId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<MyProfileResponse> getMyProfile(@AuthUserId Long userId) {
        return ResponseEntity.ok(userService.getMyProfile(userId));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> withdrawMyAccount(@AuthUserId Long userId) {
        userService.withdrawMyAccount(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/email")
    public ResponseEntity<UpdateMyEmailResponse> updateMyEmail(@AuthUserId Long userId,
                                                               @Valid @RequestBody UpdateMyEmailRequest request
    ) {
        return ResponseEntity.ok(userService.updateMyEmail(userId, request));
    }

    @DeleteMapping("/me/email")
    public ResponseEntity<Void> deleteMyEmail(@AuthUserId Long userId) {
        userService.deleteMyEmail(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/nickname")
    public ResponseEntity<UpdateMyNicknameResponse> updateMyNickname(@AuthUserId Long userId,
                                                                     @Valid @RequestBody UpdateMyNicknameRequest request
    ) {
        return ResponseEntity.ok(userService.updateMyNickname(userId, request));
    }

    @PutMapping("/me/github-url")
    public ResponseEntity<UpdateMyGithubUrlResponse> updateMyGithubUrl(@AuthUserId Long userId,
                                                                       @Valid @RequestBody UpdateMyGithubUrlRequest request
    ) {
        return ResponseEntity.ok(userService.updateMyGithubUrl(userId, request));
    }

    @DeleteMapping("/me/github-url")
    public ResponseEntity<Void> deleteMyGithubUrl(@AuthUserId Long userId) {
        userService.deleteMyGithubUrl(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/techblog-url")
    public ResponseEntity<UpdateMyTechblogUrlResponse> updateMyTechblogUrl(@AuthUserId Long userId,
                                                                           @Valid @RequestBody UpdateMyTechblogUrlRequest request
    ) {
        return ResponseEntity.ok(userService.updateMyTechblogUrl(userId, request));
    }

    @DeleteMapping("/me/techblog-url")
    public ResponseEntity<Void> deleteMyTechblogUrl(@AuthUserId Long userId) {
        userService.deleteMyTechblogUrl(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ranking")
    public ResponseEntity<PageResponse<UserProfileResponse>> getRanking(
            @RequestParam(defaultValue = "0") @Min(0) @Max(100000) int page,
            @RequestParam(defaultValue = "100") @Min(10) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(PageResponse.from(userService.getRanking(pageable)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @GetMapping("/nickname/availability")
    public ResponseEntity<Void> checkNicknameAvailability(@RequestParam @Size(min = 4, max = 20) String nickname) {
        if (userService.isNicknameDuplicated(nickname)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok().build();
    }
}
