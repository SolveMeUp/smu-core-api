package com.solvemeup.smucoreapi.domain.user.entity;

import com.solvemeup.smucoreapi.domain.user.exception.InvalidNicknameException;
import com.solvemeup.smucoreapi.global.entity.ModifiedTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.solvemeup.smucoreapi.domain.user.entity.UserRole.USER;
import static com.solvemeup.smucoreapi.domain.user.entity.UserStatus.ACTIVE;
import static com.solvemeup.smucoreapi.domain.user.entity.UserStatus.WITHDRAWN;

@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = {"oauth2_provider", "oauth2_provider_id"}),
        indexes = {
                @Index(name = "idx_users_rating_id", columnList = "rating, id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends ModifiedTimeEntity {

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
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Version
    @Column(nullable = false)
    private int version;

    public static User create(OAuth2Provider oauth2Provider,
                              String oauth2ProviderId,
                              String nickname
    ) {
        User user = new User();
        user.oauth2Provider = oauth2Provider;
        user.oauth2ProviderId = oauth2ProviderId;
        user.nickname = nickname;
        user.role = USER;
        user.status = ACTIVE;
        return user;
    }

    public void updateEmail(String email) {
        this.email = normalizeNullableString(email);
    }

    public void updateNickname(String nickname) {
        String normalized = normalizeNullableString(nickname);
        if (normalized == null) {
            throw new InvalidNicknameException();
        }

        this.nickname = normalized;
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

    public void withdraw() {
        if (this.status == WITHDRAWN) {
            return;
        }

        this.status = WITHDRAWN;
    }

    public void reactivate() {
        if (this.status != WITHDRAWN) {
            return;
        }

        this.status = ACTIVE;
    }
}
