package com.solvemeup.smucoreapi.domain.user.entity;

import com.solvemeup.smucoreapi.domain.user.enums.OAuth2Provider;
import com.solvemeup.smucoreapi.domain.user.enums.Role;
import com.solvemeup.smucoreapi.domain.user.enums.Status;
import com.solvemeup.smucoreapi.domain.user.exception.InvalidNicknameException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

import static com.solvemeup.smucoreapi.domain.user.enums.Role.USER;
import static com.solvemeup.smucoreapi.domain.user.enums.Status.ACTIVE;

@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = {"oauth2Provider", "oauth2ProviderId"}),
        indexes = {
                @Index(name = "idx_users_rating_id", columnList = "rating, id")
        }
)
@SQLRestriction("status <> 'DELETED'")
@SQLDelete(sql = "UPDATE users SET status='DELETED', deleted_at=NOW() WHERE id=? AND version=?")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

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

    public static User createUser(OAuth2Provider oauth2Provider,
                                  String oauth2ProviderId,
                                  String nickname,
                                  String profileImageUrl
    ) {
        User user = new User();
        user.oauth2Provider = oauth2Provider;
        user.oauth2ProviderId = oauth2ProviderId;
        user.nickname = nickname;
        user.profileImageUrl = profileImageUrl;
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
}
