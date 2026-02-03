package com.solvemeup.smucoreapi.global.security.annotation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * 현재 인증된 사용자 객체를 컨트롤러 파라미터로 주입한다.
 *
 * <p>Spring Security의 {@link AuthenticationPrincipal}을 기반으로 하며,
 * 인증된 Principal 객체 전체를 주입받는다.
 *
 * <p>인증이 필요 없는 요청에서는 null이 주입될 수 있다.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface AuthUser {
}
