package com.solvemeup.smucoreapi.domain.community.entity;

import com.solvemeup.smucoreapi.domain.community.enums.ReactionType;
import com.solvemeup.smucoreapi.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "post_reactions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"post_id", "user_id"})
        },
        indexes = {
                @Index(name = "idx_post_reactions_post_id", columnList = "post_id"),
                @Index(name = "idx_post_reactions_user_id", columnList = "user_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionType reactionType;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static PostReaction create(Post post, UserEntity user, ReactionType reactionType) {
        PostReaction reaction = new PostReaction();
        reaction.post = post;
        reaction.user = user;
        reaction.reactionType = reactionType;
        return reaction;
    }

    public void changeReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }
}
