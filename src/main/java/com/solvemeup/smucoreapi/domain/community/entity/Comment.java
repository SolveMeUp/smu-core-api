package com.solvemeup.smucoreapi.domain.community.entity;

import com.solvemeup.smucoreapi.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "comments",
        indexes = {
                @Index(name = "idx_comments_post_id", columnList = "post_id"),
                @Index(name = "idx_comments_user_id", columnList = "user_id"),
                @Index(name = "idx_comments_parent_id", columnList = "parent_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private int dislikeCount;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentReaction> reactions = new ArrayList<>();

    public static Comment create(Post post, User user, String content) {
        Comment comment = new Comment();
        comment.post = post;
        comment.user = user;
        comment.content = content;
        comment.likeCount = 0;
        comment.dislikeCount = 0;
        return comment;
    }

    public static Comment createReply(Post post, User user, Comment parent, String content) {
        Comment reply = new Comment();
        reply.post = post;
        reply.user = user;
        reply.parent = parent;
        reply.content = content;
        reply.likeCount = 0;
        reply.dislikeCount = 0;
        return reply;
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void incrementDislikeCount() {
        this.dislikeCount++;
    }

    public void decrementDislikeCount() {
        if (this.dislikeCount > 0) {
            this.dislikeCount--;
        }
    }

    public boolean isOwner(Long userId) {
        return this.user.getId().equals(userId);
    }

    public boolean isReply() {
        return this.parent != null;
    }
}
