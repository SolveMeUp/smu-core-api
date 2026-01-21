package com.solvemeup.smucoreapi.domain.user.controller;

import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyEmailRequest;
import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyGithubUrlRequest;
import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyNicknameRequest;
import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyTechblogUrlRequest;
import com.solvemeup.smucoreapi.domain.user.dto.response.MyProfileResponse;
import com.solvemeup.smucoreapi.domain.user.dto.response.UserProfileResponse;
import com.solvemeup.smucoreapi.domain.user.service.UserService;
import com.solvemeup.smucoreapi.global.security.annotation.AuthUserId;
import com.solvemeup.smucoreapi.global.api.dto.response.PageResponse;
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
    public ResponseEntity<Void> deleteUser(@AuthUserId Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/email")
    public ResponseEntity<Void> updateMyEmail(@AuthUserId Long userId,
                                              @Valid @RequestBody UpdateMyEmailRequest request
    ) {
        userService.updateMyEmail(userId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/email")
    public ResponseEntity<Void> deleteMyEmail(@AuthUserId Long userId) {
        userService.deleteMyEmail(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/nickname")
    public ResponseEntity<Void> updateMyNickname(@AuthUserId Long userId,
                                                 @Valid @RequestBody UpdateMyNicknameRequest request
    ) {
        userService.updateMyNickname(userId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/github-url")
    public ResponseEntity<Void> updateMyGithubUrl(@AuthUserId Long userId,
                                                  @Valid @RequestBody UpdateMyGithubUrlRequest request
    ) {
        userService.updateMyGithubUrl(userId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/github-url")
    public ResponseEntity<Void> deleteMyGithubUrl(@AuthUserId Long userId) {
        userService.deleteMyGithubUrl(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/techblog-url")
    public ResponseEntity<Void> updateMyTechblogUrl(@AuthUserId Long userId,
                                                    @Valid @RequestBody UpdateMyTechblogUrlRequest request
    ) {
        userService.updateMyTechblogUrl(userId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/techblog-url")
    public ResponseEntity<Void> deleteMyTechblogUrl(@AuthUserId Long userId) {
        userService.deleteMyTechblogUrl(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ranking")
    public ResponseEntity<PageResponse<UserProfileResponse>> getRanking(@RequestParam(defaultValue = "0") @Min(0) @Max(100000) int page,
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
