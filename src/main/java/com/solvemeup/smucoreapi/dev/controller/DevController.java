package com.solvemeup.smucoreapi.dev.controller;

import com.solvemeup.smucoreapi.domain.auth.oauth2.principal.CustomOAuth2User;
import com.solvemeup.smucoreapi.domain.user.entity.UserEntity;
import com.solvemeup.smucoreapi.domain.user.exception.UserNotFoundException;
import com.solvemeup.smucoreapi.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.solvemeup.smucoreapi.domain.auth.oauth2.principal.LoginEvent.*;

@Profile("dev")
@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
public class DevController {

    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<Void> devLogin(@RequestParam Long userId, HttpServletRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        CustomOAuth2User principal = new CustomOAuth2User(user.getId(), user.getRole(), NONE);

        var auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> devLogout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok().build();
    }
}
