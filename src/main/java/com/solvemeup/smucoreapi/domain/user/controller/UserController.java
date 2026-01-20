package com.solvemeup.smucoreapi.domain.user.controller;

import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyEmailRequestDTO;
import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyGithubUrlRequestDTO;
import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyNicknameRequestDTO;
import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyTechblogUrlRequestDTO;
import com.solvemeup.smucoreapi.domain.user.dto.response.MyProfileResponseDTO;
import com.solvemeup.smucoreapi.domain.user.dto.response.NicknameAvailabilityResponseDTO;
import com.solvemeup.smucoreapi.domain.user.dto.response.UserProfileResponseDTO;
import com.solvemeup.smucoreapi.domain.user.service.UserService;
import com.solvemeup.smucoreapi.global.auth.AuthUserId;
import com.solvemeup.smucoreapi.global.dto.PageResponse;
import jakarta.validation.Valid;
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

    @PutMapping("/me/email")
    public ResponseEntity<Void> updateMyEmail(@AuthUserId Long userId,
                                              @Valid @RequestBody UpdateMyEmailRequestDTO requestDTO
    ) {
        userService.updateMyEmail(userId, requestDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me/email")
    public ResponseEntity<Void> deleteMyEmail(@AuthUserId Long userId) {
        userService.deleteMyEmail(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/nickname")
    public ResponseEntity<Void> updateMyNickname(@AuthUserId Long userId,
                                                 @Valid @RequestBody UpdateMyNicknameRequestDTO requestDTO
    ) {
        userService.updateMyNickname(userId, requestDTO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/github-url")
    public ResponseEntity<Void> updateMyGithubUrl(@AuthUserId Long userId,
                                                  @RequestBody UpdateMyGithubUrlRequestDTO request
    ) {
        userService.updateMyGithubUrl(userId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me/github-url")
    public ResponseEntity<Void> deleteMyGithubUrl(@AuthUserId Long userId) {
        userService.deleteMyGithubUrl(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/techblog-url")
    public ResponseEntity<Void> updateMyTechblogUrl(@AuthUserId Long userId,
                                                    @RequestBody UpdateMyTechblogUrlRequestDTO request
    ) {
        userService.updateMyTechblogUrl(userId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me/techblog-url")
    public ResponseEntity<Void> deleteMyTechblogUrl(@AuthUserId Long userId) {
        userService.deleteMyTechblogUrl(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ranking")
    public ResponseEntity<PageResponse<UserProfileResponseDTO>> getRanking(@RequestParam(defaultValue = "0") @Min(0) @Max(100000) int page,
                                                                           @RequestParam(defaultValue = "100") @Min(10) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(PageResponse.from(userService.getRanking(pageable)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDTO> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/nickname/availability")
    public ResponseEntity<NicknameAvailabilityResponseDTO> checkNicknameAvailability(@RequestParam @Size(min = 4, max = 20) String nickname) {
        boolean available = userService.checkNicknameAvailability(nickname);
        return ResponseEntity.ok(NicknameAvailabilityResponseDTO.of(nickname, available));
    }
}
