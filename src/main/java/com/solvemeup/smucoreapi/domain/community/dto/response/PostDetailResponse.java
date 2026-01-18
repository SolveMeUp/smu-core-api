package com.solvemeup.smucoreapi.domain.community.dto.response;

import com.solvemeup.smucoreapi.domain.community.entity.Post;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostDetailResponse(
        Long id,
        String title,
        String content,
        int viewCount,
        int likeCount,
        int dislikeCount,
        int commentCount,
        AuthorResponse author,
        List<CommentResponse> comments,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostDetailResponse of(Post post, List<CommentResponse> comments) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .dislikeCount(post.getDislikeCount())
                .commentCount(post.getCommentCount())
                .author(AuthorResponse.from(post.getUser()))
                .comments(comments)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
