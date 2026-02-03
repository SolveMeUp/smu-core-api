package com.solvemeup.smucoreapi.domain.auth.controller;

import com.solvemeup.smucoreapi.domain.auth.dto.response.MyAuthResponse;
import com.solvemeup.smucoreapi.domain.auth.oauth2.principal.CustomOAuth2User;
import com.solvemeup.smucoreapi.global.security.annotation.AuthUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 상태 조회 API 컨트롤러.
 *
 * <p>현재 로그인한 사용자의 인증 정보(userId, 권한, 로그인 이벤트)를 반환한다.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/me")
    public MyAuthResponse getMyAuth(@AuthUser CustomOAuth2User user) {
        return new MyAuthResponse(user.getUserId(), user.getRole(), user.getLoginEvent());
    }
}
