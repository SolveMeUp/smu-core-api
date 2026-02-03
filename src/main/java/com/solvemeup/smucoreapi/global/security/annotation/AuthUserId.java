package com.solvemeup.smucoreapi.global.security.annotation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * 현재 인증된 사용자의 식별자(userId)를 컨트롤러 파라미터로 주입한다.
 *
 * <p>Spring Security의 {@link AuthenticationPrincipal}에서
 * {@code userId} 속성만 추출하여 주입한다.
 *
 * <p>인증이 필요 없는 요청에서는 null이 주입될 수 있다.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal(expression = "userId")
public @interface AuthUserId {
}
