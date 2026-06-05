package com.solvemeup.smucoreapi.domain.auth.oauth2.userinfo;

import com.solvemeup.smucoreapi.domain.auth.exception.OAuth2AttributeMissingException;
import com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider.GITHUB;

/**
 * GitHub OAuth2 응답을 표현하는 {@link OAuth2UserInfo} 구현체.
 *
 * <p>GitHub이 내려준 attributes 맵에서 사용자 식별 ID를 추출한다.
 */
@RequiredArgsConstructor
public final class GithubOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    @Override
    public OAuth2Provider getOAuth2Provider() {
        return GITHUB;
    }

    @Override
    public String getOAuth2ProviderId() {
        return requiredString("id");
    }

    /**
     * attributes에서 필수 문자열 속성을 꺼낸다.
     *
     * @throws OAuth2AttributeMissingException 해당 속성이 없을 경우
     */
    private String requiredString(String key) {
        Object value = attributes.get(key);
        if (value == null) {
            throw new OAuth2AttributeMissingException(GITHUB, key);
        }
        return value.toString();
    }
}
