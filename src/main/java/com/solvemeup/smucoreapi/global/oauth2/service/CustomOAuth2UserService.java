package com.solvemeup.smucoreapi.global.oauth2.service;

import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.repository.UserRepository;
import com.solvemeup.smucoreapi.global.oauth2.dto.GithubResponseDTO;
import com.solvemeup.smucoreapi.global.oauth2.session.UserSession;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        GithubResponseDTO githubResponseDTO = new GithubResponseDTO(oAuth2User.getAttributes());

        User user = userRepository
                .findByOauth2ProviderAndOauth2ProviderId(
                        githubResponseDTO.getOAuth2Provider(),
                        githubResponseDTO.getOAuth2ProviderId()
                )
                .orElseGet(() -> userRepository.save(
                        User.createUser(
                                githubResponseDTO.getOAuth2Provider(),
                                githubResponseDTO.getOAuth2ProviderId(),
                                githubResponseDTO.getNickname(),
                                githubResponseDTO.getProfileImageUrl()
                        )
                ));

        httpSession.setAttribute(
                "USER_SESSION",
                new UserSession(user.getId(), user.getRole())
        );

        return oAuth2User;
    }
}
