package com.solvemeup.smucoreapi.domain.auth.oauth2.service;

import com.solvemeup.smucoreapi.domain.auth.exception.OAuth2LoginException;
import com.solvemeup.smucoreapi.domain.auth.oauth2.userinfo.OAuth2UserInfo;
import com.solvemeup.smucoreapi.domain.auth.oauth2.userinfo.OAuth2UserInfoFactory;
import com.solvemeup.smucoreapi.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.*;

/**
 * OAuth2 로그인 시 공급자 사용자 정보를 로드하는 커스텀 OAuth2UserService.
 *
 * <p>공급자 userinfo 조회(HTTP)는 트랜잭션 밖에서 수행하고,
 * 사용자 계정 연동(조회·생성·복구)은 {@link OAuth2UserResolver}에 위임하여
 * 별도 트랜잭션으로 처리한다. (HTTP 호출이 DB 트랜잭션 안에서 커넥션을 점유하지 않도록 분리.)
 *
 * <p>처리 중 발생한 도메인 예외는 {@link OAuth2AuthenticationException}으로 변환되어
 * Spring Security 인증 실패 흐름으로 전달된다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuth2UserResolver oauth2UserResolver;

    /**
     * OAuth2 인증 요청을 처리하여 애플리케이션 인증 Principal을 반환한다.
     *
     * @throws OAuth2AuthenticationException OAuth2 인증 과정에서 오류가 발생한 경우
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            OAuth2UserInfo userInfo = parseOAuth2UserInfo(userRequest);
            return oauth2UserResolver.resolve(userInfo);
        } catch (OAuth2LoginException e) {
            throw wrapOAuth2AuthenticationException(e.getErrorCode(), e);
        } catch (DataIntegrityViolationException e) {
            throw wrapOAuth2AuthenticationException(OAUTH2_LOGIN_FAILED, e);
        }
    }

    /**
     * 공급자 userinfo를 조회(HTTP)하여 {@link OAuth2UserInfo}로 변환한다.
     *
     * <p>{@link DefaultOAuth2UserService}가 로드한 원본 attributes를 공급자별 구현체로 매핑한다.
     */
    private OAuth2UserInfo parseOAuth2UserInfo(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.debug("OAuth2 registrationId={}", userRequest.getClientRegistration().getRegistrationId());
        log.debug("OAuth2 attributes keys={}", oAuth2User.getAttributes().keySet());

        return OAuth2UserInfoFactory.of(
                userRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes()
        );
    }

    /**
     * 도메인 예외를 Spring Security가 처리하는 {@link OAuth2AuthenticationException}으로 변환한다.
     *
     * <p>{@link ErrorCode}를 {@link OAuth2Error}에 실어 인증 실패 핸들러로 전달한다.
     */
    private OAuth2AuthenticationException wrapOAuth2AuthenticationException(ErrorCode errorCode, RuntimeException e) {
        return new OAuth2AuthenticationException(
                new OAuth2Error(errorCode.getCode(), errorCode.getDefaultMessage(), null),
                errorCode.getDefaultMessage(),
                e
        );
    }
}
