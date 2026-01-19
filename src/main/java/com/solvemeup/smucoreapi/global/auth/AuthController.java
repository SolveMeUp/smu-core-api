package com.solvemeup.smucoreapi.global.auth;

import com.solvemeup.smucoreapi.global.oauth2.principal.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/me")
    public ResponseEntity<MeResponseDTO> me(@AuthUser CustomOAuth2User user) {
        return ResponseEntity.ok(new MeResponseDTO(user.getUserId(), user.getRole()));
    }
}
