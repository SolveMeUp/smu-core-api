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

    Optional<User> findByOauth2ProviderAndOauth2ProviderId(OAuth2Provider oauth2Provider, String oauth2ProviderId);

    Optional<User> findByNickname(String nickname);

    @Query(
            value = """
                    SELECT
                        u.nickname AS nickname,
                        u.profile_image_url AS profileImageUrl,
                        u.github_url AS githubUrl,
                        u.techblog_url AS techblogUrl,
                        u.rating AS rating,
                        u.role AS role,
                        u.status AS status,
                        RANK() OVER (ORDER BY u.rating DESC) AS `rank`
                    FROM users u
                    WHERE u.status != 'DELETED'
                    ORDER BY u.rating DESC, u.id
                    """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM users u
                    WHERE u.status != 'DELETED'
                    """,
            nativeQuery = true
    )
    Page<UserRankingProjection> findRankingWithRank(Pageable pageable);

    @Query(value = """
            SELECT COUNT(*) + 1
            FROM users u
            WHERE u.status != 'DELETED' AND u.rating > :rating
            """, nativeQuery = true)
    long calcCompetitionRankByRating(@Param("rating") int rating);
}
