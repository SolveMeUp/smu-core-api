package com.solvemeup.smucoreapi.domain.user.repository;

import com.solvemeup.smucoreapi.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 서비스에 노출되는 사용자 조회 전용 리포지토리.
 *
 * <p>ACTIVE, BLOCKED 상태의 사용자만 조회 대상이며,
 * 탈퇴(DELETED) 및 익명화(ANONYMIZED) 사용자는 제외한다.
 *
 * <p>랭킹 조회 시 {@code rank} 값은 전체 사용자 기준의 순위를 의미한다.
 */
public interface UserRepository extends JpaRepository<User, Long> {

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
