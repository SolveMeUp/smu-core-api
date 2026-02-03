package com.solvemeup.smucoreapi.domain.auth.oauth2.response;

import com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider;
import com.solvemeup.smucoreapi.domain.auth.oauth2.exception.OAuth2AttributeMissingException;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider.GITHUB;

/**
 * GitHub OAuth2 사용자 응답을 표현하는 구현체.
 *
 * <p>GitHub OAuth2 응답의 attributes 맵에서
 * 애플리케이션에 필요한 사용자 식별 정보를 추출한다.
 */
@Slf4j
@RequiredArgsConstructor
@ToString
public final class GithubResponse implements OAuth2Response {

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
     * OAuth2 응답에서 필수 문자열 속성을 조회한다.
     *
     * @throws OAuth2AttributeMissingException 필수 속성이 존재하지 않을 경우
     */
    private String requiredString(String key) {
        log.debug("Required attribute '{}' found={}", key, attributes.containsKey(key));

        Object value = attributes.get(key);
        if (value == null) {
            throw new OAuth2AttributeMissingException(GITHUB, key);
        }
        return value.toString();
    }
}
