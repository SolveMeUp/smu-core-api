package com.solvemeup.smucoreapi.domain.auth.controller;

import com.solvemeup.smucoreapi.domain.auth.dto.response.MyAuthResponse;
import com.solvemeup.smucoreapi.domain.auth.oauth2.principal.CustomOAuth2User;
import com.solvemeup.smucoreapi.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 상태 조회 API 컨트롤러.
 *
 * <p>현재 로그인한 사용자의 인증 정보(userId, 권한)를 반환한다.
 */
@Tag(name = "Auth", description = "로그인 사용자의 인증 상태 조회 API")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Operation(
            summary = "내 인증 정보 조회",
            description = """
                    현재 로그인 세션의 인증 정보(userId, 권한)를 반환한다.

                    프론트가 앱 진입 시 로그인 여부를 확인하는 용도로 호출한다.
                    인증된 세션이 없으면 401(Unauthorized)을 반환한다.""")
    @GetMapping("/me")
    public ResponseEntity<MyAuthResponse> getMyAuth(@AuthUser CustomOAuth2User user) {
        return ResponseEntity.ok(new MyAuthResponse(user.userId(), user.role()));
    }
}
