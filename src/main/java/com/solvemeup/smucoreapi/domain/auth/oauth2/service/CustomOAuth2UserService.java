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

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final NicknameGenerator nicknameGenerator;

    private static final int RETRY_LIMIT = 3;

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

    private OAuth2AuthenticationException wrapOAuth2AuthenticationException(ErrorCode errorCode, RuntimeException e) {
        return new OAuth2AuthenticationException(new OAuth2Error(errorCode.getCode(), errorCode.getDefaultMessage(), null), errorCode.getDefaultMessage(), e);
    }
}
