package com.solvemeup.smucoreapi.domain.auth.oauth2.service;

import com.solvemeup.smucoreapi.domain.auth.exception.BlockedUserException;
import com.solvemeup.smucoreapi.domain.auth.exception.NicknameGenerationFailedException;
import com.solvemeup.smucoreapi.domain.auth.oauth2.exception.InvalidOAuth2RegistrationIdException;
import com.solvemeup.smucoreapi.domain.auth.oauth2.exception.OAuth2AttributeMissingException;
import com.solvemeup.smucoreapi.domain.auth.oauth2.exception.UnsupportedOAuth2ProviderException;
import com.solvemeup.smucoreapi.domain.auth.oauth2.principal.CustomOAuth2User;
import com.solvemeup.smucoreapi.domain.auth.oauth2.response.OAuth2Response;
import com.solvemeup.smucoreapi.domain.auth.oauth2.response.OAuth2ResponseFactory;
import com.solvemeup.smucoreapi.domain.user.entity.UserEntity;
import com.solvemeup.smucoreapi.domain.user.repository.UserRepository;
import com.solvemeup.smucoreapi.domain.user.util.NicknameGenerator;
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
import org.springframework.transaction.annotation.Transactional;

import static com.solvemeup.smucoreapi.domain.auth.oauth2.principal.LoginEvent.*;
import static com.solvemeup.smucoreapi.domain.user.entity.Status.*;
import static com.solvemeup.smucoreapi.global.exception.ErrorCode.*;

/**
 * OAuth2 로그인 과정에서 사용자 정보를 로드하고
 * 애플리케이션 사용자 도메인과 연동하는 커스텀 OAuth2UserService.
 *
 * <p>OAuth2 공급자(Google, GitHub 등)로부터 전달받은 사용자 정보를 기반으로
 * 사용자를 조회하거나 신규 생성하며, 사용자 상태에 따라 다음과 같이 처리한다:
 *
 * <ul>
 *   <li>신규 사용자: 랜덤 닉네임을 부여하여 회원 가입</li>
 *   <li>차단된 사용자(BLOCKED): 로그인 차단</li>
 *   <li>탈퇴 사용자(DELETED): 계정 복구 후 로그인</li>
 *   <li>익명화 사용자(ANONYMIZED): 계정 복구 후 로그인</li>
 * </ul>
 *
 * <p>도메인 계층에서 발생한 예외는
 * {@link org.springframework.security.oauth2.core.OAuth2AuthenticationException}으로 변환되어
 * Spring Security 인증 흐름으로 전달된다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final NicknameGenerator nicknameGenerator;

    private static final int RETRY_LIMIT = 3;

    /**
     * OAuth2 인증 요청을 처리하여 애플리케이션 내부 사용자 정보를 로드한다.
     *
     * <p>OAuth2 공급자 응답을 파싱한 뒤, 사용자 조회, 생성, 복구 또는 차단 여부를 판단하여 {@link CustomOAuth2User}를 반환한다.
     *
     * @throws OAuth2AuthenticationException OAuth2 인증 과정에서 오류가 발생한 경우
     */
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            OAuth2User oAuth2User = super.loadUser(userRequest);

            OAuth2Response response = OAuth2ResponseFactory.of(
                    userRequest.getClientRegistration().getRegistrationId(),
                    oAuth2User.getAttributes()
            );

            UserEntity user = userRepository
                    .findIncludingDeletedByOauth2ProviderAndOauth2ProviderId(
                            response.getOAuth2Provider(),
                            response.getOAuth2ProviderId()
                    )
                    .orElse(null);

            if (user == null) {
                user = createUserWithUniqueNickname(response);
                return new CustomOAuth2User(user.getId(), user.getRole(), SIGNED_UP);
            } else if (user.getStatus() == BLOCKED) {
                throw new BlockedUserException(user.getId());
            } else if (user.getStatus() == DELETED) {
                user.restoreDeletedUser();
                return new CustomOAuth2User(user.getId(), user.getRole(), RESTORED_FROM_DELETED);
            } else if (user.getStatus() == ANONYMIZED) {
                user.activateAnonymizedUser();
                return new CustomOAuth2User(user.getId(), user.getRole(), RESTORED_FROM_ANONYMIZED);
            }

            return new CustomOAuth2User(user.getId(), user.getRole(), NONE);
        } catch (InvalidOAuth2RegistrationIdException e) {
            throw wrapOAuth2AuthenticationException(OAUTH2_INVALID_REGISTRATION_ID, e);
        } catch (UnsupportedOAuth2ProviderException e) {
            throw wrapOAuth2AuthenticationException(OAUTH2_UNSUPPORTED_PROVIDER, e);
        } catch (OAuth2AttributeMissingException e) {
            throw wrapOAuth2AuthenticationException(OAUTH2_MISSING_ATTRIBUTE, e);
        } catch (BlockedUserException e) {
            throw wrapOAuth2AuthenticationException(AUTH_BLOCKED_USER, e);
        } catch (NicknameGenerationFailedException e) {
            throw wrapOAuth2AuthenticationException(NICKNAME_GENERATION_FAILED, e);
        }
    }

    private UserEntity createUserWithUniqueNickname(OAuth2Response response) {
        DataIntegrityViolationException lastException = null;

        for (int attempt = 1; attempt <= RETRY_LIMIT; attempt++) {
            try {
                log.debug("Nickname generation attempt {}", attempt);

                return userRepository.save(
                        UserEntity.createUser(
                                response.getOAuth2Provider(),
                                response.getOAuth2ProviderId(),
                                nicknameGenerator.generate()
                        )
                );
            } catch (DataIntegrityViolationException e) {
                lastException = e;
            }
        }

        throw new NicknameGenerationFailedException(RETRY_LIMIT, lastException);
    }

    /**
     * 도메인 예외를 OAuth2 인증 예외로 변환한다.
     *
     * <p>에러 코드는 {@link ErrorCode}를 기반으로 하며, Spring Security 인증 실패 처리 흐름으로 전달된다.
     */
    private OAuth2AuthenticationException wrapOAuth2AuthenticationException(ErrorCode errorCode, RuntimeException e) {
        return new OAuth2AuthenticationException(new OAuth2Error(errorCode.getCode(), errorCode.getDefaultMessage(), null), errorCode.getDefaultMessage(), e);
    }
}
