package com.solvemeup.smucoreapi.domain.user.entity;

import com.solvemeup.smucoreapi.domain.user.exception.InvalidNicknameException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

import static com.solvemeup.smucoreapi.domain.user.entity.Role.USER;
import static com.solvemeup.smucoreapi.domain.user.entity.Status.*;

/**
 * 사용자 도메인 엔티티.
 *
 * <p>OAuth2 기반 인증 사용자를 표현하며,
 * 소프트 딜리트와 상태 기반 접근 제어를 지원한다.
 *
 * <p>사용자 상태:
 * <ul>
 *   <li>{@code ACTIVE} : 정상 사용자</li>
 *   <li>{@code BLOCKED} : 로그인만 제한된 사용자</li>
 *   <li>{@code DELETED} : 탈퇴 처리된 사용자</li>
 *   <li>{@code ANONYMIZED} : 개인정보가 제거된 사용자</li>
 * </ul>
 */
@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = {"oauth2_provider", "oauth2_provider_id"}),
        indexes = {
                @Index(name = "idx_users_rating_id", columnList = "rating, id")
        }
)
@SQLRestriction("status NOT IN ('DELETED', 'ANONYMIZED')")
@SQLDelete(sql = "UPDATE users SET status='DELETED', deleted_at=NOW() WHERE id=? AND version=?")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OAuth2Provider oauth2Provider;

    @Column(nullable = false)
    private String oauth2ProviderId;

    private String email;

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @Column(length = 2083)
    private String profileImageUrl;

    @Column(length = 2083)
    private String githubUrl;

    @Column(length = 2083)
    private String techblogUrl;

    @Column(nullable = false)
    private int rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Version
    @Column(nullable = false)
    private int version;

    public static UserEntity createUser(OAuth2Provider oauth2Provider,
                                        String oauth2ProviderId,
                                        String nickname
    ) {
        UserEntity user = new UserEntity();
        user.oauth2Provider = oauth2Provider;
        user.oauth2ProviderId = oauth2ProviderId;
        user.nickname = nickname;
        user.role = USER;
        user.status = ACTIVE;
        user.rating = 0;
        return user;
    }

    public void updateEmail(String email) {
        this.email = normalizeNullableString(email);
    }

    public void updateNickname(String nickname) {
        if (nickname == null || nickname.trim().isBlank()) {
            throw new InvalidNicknameException();
        }

        this.nickname = nickname.trim();
    }

    public void updateGithubUrl(String githubUrl) {
        this.githubUrl = normalizeNullableString(githubUrl);
    }

    public void updateTechblogUrl(String techblogUrl) {
        this.techblogUrl = normalizeNullableString(techblogUrl);
    }

    private String normalizeNullableString(String value) {
        if (value == null) return null;

        String trimmed = value.trim();
        return trimmed.isBlank() ? null : trimmed;
    }

    public void restoreDeletedUser() {
        if (this.status != DELETED) {
            return;
        }

        this.status = ACTIVE;
        this.deletedAt = null;
    }

    public void activateAnonymizedUser() {
        if (this.status != ANONYMIZED) {
            return;
        }

        this.status = ACTIVE;
        this.deletedAt = null;
    }
}
