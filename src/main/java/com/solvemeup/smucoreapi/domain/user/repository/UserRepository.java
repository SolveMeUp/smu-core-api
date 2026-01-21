package com.solvemeup.smucoreapi.domain.user.repository;

import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.enums.OAuth2Provider;
import com.solvemeup.smucoreapi.domain.user.repository.projection.UserRankingProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

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
            SELECT EXISTS (
                SELECT 1
                FROM users
                WHERE nickname = :nickname
            )
            """, nativeQuery = true)
    Long existsIncludingDeletedByNickname(@Param("nickname") String nickname);

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
                u.status AS status,
                RANK() OVER (ORDER BY u.rating DESC) AS `rank`
            FROM users u
            WHERE u.status NOT IN ('DELETED', 'ANONYMIZED')
            ORDER BY u.rating DESC, u.id
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM users u
                    WHERE u.status NOT IN ('DELETED', 'ANONYMIZED')
                    """,
            nativeQuery = true
    )
    Page<UserRankingProjection> findUserRankingPageWithRank(Pageable pageable);

    @Query(value = """
            SELECT COUNT(*) + 1
            FROM users u
            WHERE u.status NOT IN ('DELETED', 'ANONYMIZED') AND u.rating > :rating
            """, nativeQuery = true)
    long calculateCompetitionRankByRating(@Param("rating") int rating);
}
