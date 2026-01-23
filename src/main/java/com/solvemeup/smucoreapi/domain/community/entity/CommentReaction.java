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
        name = "comment_reactions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"comment_id", "user_id"})
        },
        indexes = {
                @Index(name = "idx_comment_reactions_comment_id", columnList = "comment_id"),
                @Index(name = "idx_comment_reactions_user_id", columnList = "user_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionType reactionType;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static CommentReaction create(Comment comment, UserEntity user, ReactionType reactionType) {
        CommentReaction reaction = new CommentReaction();
        reaction.comment = comment;
        reaction.user = user;
        reaction.reactionType = reactionType;
        return reaction;
    }

    public void changeReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }
}
