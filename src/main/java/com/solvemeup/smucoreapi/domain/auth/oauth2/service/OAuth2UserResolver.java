package com.solvemeup.smucoreapi.domain.auth.oauth2.service;

import com.solvemeup.smucoreapi.domain.auth.exception.BlockedUserException;
import com.solvemeup.smucoreapi.domain.auth.oauth2.principal.CustomOAuth2User;
import com.solvemeup.smucoreapi.domain.auth.oauth2.userinfo.OAuth2UserInfo;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.repository.UserInternalRepository;
import com.solvemeup.smucoreapi.domain.user.repository.UserRepository;
import com.solvemeup.smucoreapi.domain.user.util.NicknameGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.solvemeup.smucoreapi.domain.user.entity.Status.*;

/**
 * OAuth2 식별 정보를 애플리케이션 사용자 계정과 연동하는 트랜잭션 경계.
 *
 * <p>공급자 HTTP 호출과 분리된 DB 작업(조회·생성·복구)을 하나의 트랜잭션으로 처리한다.
 * 사용자 상태에 따라:
 * <ul>
 *   <li>신규: 랜덤 닉네임을 부여하여 가입</li>
 *   <li>BLOCKED: 로그인 차단</li>
 *   <li>DELETED: 계정 복구 후 로그인</li>
 * </ul>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserResolver {

    private final UserRepository userRepository;
    private final UserInternalRepository userInternalRepository;
    private final NicknameGenerator nicknameGenerator;

    /**
     * OAuth2 식별 정보로 사용자를 조회·생성·복구하여 인증 Principal을 만든다.
     *
     * <p>소프트딜리트 복구는 native 조회로 가져온 managed 엔티티의 상태를 변경하여
     * dirty-checking UPDATE로 처리한다. ({@code merge}/{@code save}는
     * {@code @SQLRestriction}이 SELECT를 걸러내 복구가 불가능하다.)
     *
     * @throws BlockedUserException 차단된 사용자인 경우
     */
    @Transactional
    public CustomOAuth2User resolve(OAuth2UserInfo userInfo) {
        User user = userInternalRepository
                .findIncludingDeletedByOauth2ProviderAndOauth2ProviderId(
                        userInfo.getOAuth2Provider(),
                        userInfo.getOAuth2ProviderId()
                )
                .orElse(null);

        if (user == null) {
            User created = userRepository.save(
                    User.createUser(
                            userInfo.getOAuth2Provider(),
                            userInfo.getOAuth2ProviderId(),
                            nicknameGenerator.generate()
                    )
            );
            return toPrincipal(created);
        }

        if (user.getStatus() == BLOCKED) {
            throw new BlockedUserException();
        }

        if (user.getStatus() == DELETED) {
            user.restoreFromDeleted();
        }

        return toPrincipal(user);
    }

    private CustomOAuth2User toPrincipal(User user) {
        log.debug("OAuth2 login result: userId={}, status={}", user.getId(), user.getStatus());

        return new CustomOAuth2User(user.getId(), user.getRole());
    }
}
