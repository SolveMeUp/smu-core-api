package com.solvemeup.smucoreapi.domain.user.repository;

import com.solvemeup.smucoreapi.domain.user.entity.OAuth2Provider;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.entity.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndStatusNot(Long id, UserStatus status);

    Optional<User> findByOauth2ProviderAndOauth2ProviderId(OAuth2Provider oauth2Provider, String oauth2ProviderId);

    boolean existsByNickname(String nickname);

    long countByStatusNotAndRatingGreaterThan(UserStatus status, int rating);

    @Query(value = """
            SELECT
                u.id AS id,
                u.oauth2_provider AS oauth2Provider,
                u.nickname AS nickname,
                u.profile_image_url AS profileImageUrl,
                u.github_url AS githubUrl,
                u.techblog_url AS techblogUrl,
                u.rating AS rating,
                u.role AS role,
                RANK() OVER (ORDER BY u.rating DESC) AS `rank`
            FROM users u
            WHERE u.status <> 'WITHDRAWN'
            ORDER BY u.rating DESC, u.id
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM users u
                    WHERE u.status <> 'WITHDRAWN'
                    """,
            nativeQuery = true
    )
    Page<UserRankingProjection> findUserRankingPageWithRank(Pageable pageable);
}
