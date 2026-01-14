package com.solvemeup.smucoreapi.domain.user.controller;

import com.solvemeup.smucoreapi.domain.user.dto.response.MyProfileResponseDTO;
import com.solvemeup.smucoreapi.domain.user.dto.response.UserProfileResponseDTO;
import com.solvemeup.smucoreapi.domain.user.service.UserService;
import com.solvemeup.smucoreapi.global.oauth2.annotation.AuthUser;
import com.solvemeup.smucoreapi.global.oauth2.session.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<MyProfileResponseDTO> getMyProfile(@AuthUser UserSession userSession) {
        return ResponseEntity.ok(userService.getMyProfile(userSession.id()));
    }

    @GetMapping("/{nickname}")
    public ResponseEntity<UserProfileResponseDTO> getUserProfile(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.getUserProfile(nickname));
    }
}
