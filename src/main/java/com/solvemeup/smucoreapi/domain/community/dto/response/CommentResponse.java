package com.solvemeup.smucoreapi.domain.community.dto.response;

import com.solvemeup.smucoreapi.domain.community.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CommentResponse(
        Long id,
        String content,
        int likeCount,
        int dislikeCount,
        AuthorResponse author,
        List<CommentResponse> replies,
        LocalDateTime createdAt
) {
    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .likeCount(comment.getLikeCount())
                .dislikeCount(comment.getDislikeCount())
                .author(AuthorResponse.from(comment.getUser()))
                .replies(List.of())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static CommentResponse of(Comment comment, List<CommentResponse> replies) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .likeCount(comment.getLikeCount())
                .dislikeCount(comment.getDislikeCount())
                .author(AuthorResponse.from(comment.getUser()))
                .replies(replies)
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
