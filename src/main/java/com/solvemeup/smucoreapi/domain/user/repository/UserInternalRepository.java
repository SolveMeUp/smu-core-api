package com.solvemeup.smucoreapi.domain.user.repository;

import com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * 사용자 도메인의 내부 전용 조회 리포지토리.
 *
 * <p>삭제(DELETED) 및 익명화(ANONYMIZED) 상태의 사용자까지
 * 포함하여 조회가 필요한 경우에만 사용한다.
 *
 * <p>일반 서비스 로직에서는 {@code UserRepository} 사용을 권장한다.
 */
public interface UserInternalRepository extends JpaRepository<User, Long> {

    @Query(value = """
            SELECT *
            FROM users
            WHERE oauth2_provider = :oauth2Provider AND oauth2_provider_id = :oauth2ProviderId
            """, nativeQuery = true)
    Optional<User> findIncludingDeletedByOauth2ProviderAndOauth2ProviderId(
            @Param("oauth2Provider") OAuth2Provider oauth2Provider,
            @Param("oauth2ProviderId") String oauth2ProviderId
    );

    @Query(value = """
            SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END
            FROM users
            WHERE nickname = :nickname
            """, nativeQuery = true)
    boolean existsIncludingDeletedByNickname(String nickname);
}
