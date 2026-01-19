package com.solvemeup.smucoreapi.global.oauth2.service;

import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.exception.NicknameGenerationFailedException;
import com.solvemeup.smucoreapi.domain.user.repository.UserRepository;
import com.solvemeup.smucoreapi.domain.user.util.NicknameGenerator;
import com.solvemeup.smucoreapi.global.oauth2.principal.CustomOAuth2User;
import com.solvemeup.smucoreapi.global.oauth2.response.OAuth2Response;
import com.solvemeup.smucoreapi.global.oauth2.response.OAuth2ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final NicknameGenerator nicknameGenerator;

    private static final int RETRY_LIMIT = 5;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2Response response = OAuth2ResponseFactory.of(
                userRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes()
        );

        User user = userRepository
                .findByOauth2ProviderAndOauth2ProviderId(
                        response.getOAuth2Provider(),
                        response.getOAuth2ProviderId()
                )
                .orElseGet(() -> createUserWithUniqueNickname(response));

        return new CustomOAuth2User(user.getId(), user.getRole());
    }

    private User createUserWithUniqueNickname(OAuth2Response response) {
        DataIntegrityViolationException lastException = null;

        for (int attempt = 1; attempt <= RETRY_LIMIT; attempt++) {
            try {
                return userRepository.save(
                        User.createUser(
                                response.getOAuth2Provider(),
                                response.getOAuth2ProviderId(),
                                nicknameGenerator.generate(),
                                response.getProfileImageUrl()
                        )
                );
            } catch (DataIntegrityViolationException e) {
                lastException = e;

                User existing = userRepository
                        .findByOauth2ProviderAndOauth2ProviderId(
                                response.getOAuth2Provider(),
                                response.getOAuth2ProviderId()
                        )
                        .orElse(null);

                if (existing != null) {
                    return existing;
                }
            }
        }

        throw new NicknameGenerationFailedException(RETRY_LIMIT, lastException);
    }
}
