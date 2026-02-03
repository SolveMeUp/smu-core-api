package com.solvemeup.smucoreapi.domain.auth.controller;

import com.solvemeup.smucoreapi.domain.auth.dto.response.MyAuthResponse;
import com.solvemeup.smucoreapi.domain.auth.oauth2.principal.CustomOAuth2User;
import com.solvemeup.smucoreapi.global.security.annotation.AuthUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/me")
    public MyAuthResponse getMyAuth(@AuthUser CustomOAuth2User user) {
        return new MyAuthResponse(user.getUserId(), user.getRole(), user.getLoginEvent());
    }
}
