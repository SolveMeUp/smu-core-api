package com.solvemeup.smucoreapi.domain.community.dto.response;

import com.solvemeup.smucoreapi.domain.community.entity.Post;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostResponse(
        Long id,
        String title,
        String content,
        int viewCount,
        int likeCount,
        int dislikeCount,
        int commentCount,
        AuthorResponse author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .dislikeCount(post.getDislikeCount())
                .commentCount(post.getCommentCount())
                .author(AuthorResponse.from(post.getUser()))
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
