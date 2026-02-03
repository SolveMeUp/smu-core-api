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
public class User extends SoftDeleteEntity {

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
    @Column(nullable = false, length = 20)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
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

    /**
     * 사용자가 차단(BLOCKED) 상태인지 여부를 반환한다.
     */
    public boolean isBlocked() {
        return this.status == BLOCKED;
    }

    /**
     * 탈퇴(DELETED) 상태의 사용자를 복구한다.
     *
     * <p>DELETED 상태가 아닐 경우 아무 동작도 하지 않는다.
     */
    public void restoreFromDeleted() {
        if (this.status != DELETED) {
            return;
        }

        this.status = ACTIVE;
        super.restore();
    }

    /**
     * 익명화(ANONYMIZED)된 사용자를 다시 활성화한다.
     *
     * <p>ANONYMIZED 상태가 아닐 경우 아무 동작도 하지 않는다.
     */
    public void restoreFromAnonymized() {
        if (this.status != ANONYMIZED) {
            return;
        }

        this.status = ACTIVE;
        super.restore();
    }
}
