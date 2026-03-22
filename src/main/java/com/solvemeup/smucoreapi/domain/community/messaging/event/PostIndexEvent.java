package com.solvemeup.smucoreapi.domain.community.messaging.event;

import com.solvemeup.smucoreapi.domain.community.entity.Post;

import java.time.LocalDateTime;

public record PostIndexEvent(
        Long postId,
        String title,
        String content,
        String authorName,
        LocalDateTime createdAt,
        EventType eventType
) {
    public enum EventType { INDEX, UPDATE, DELETE }

    public static PostIndexEvent index(Post post) {
        return new PostIndexEvent(
                post.getId(), post.getTitle(), post.getContent(),
                post.getUser().getNickname(), post.getCreatedAt(), EventType.INDEX
        );
    }

    public static PostIndexEvent update(Post post) {
        return new PostIndexEvent(
                post.getId(), post.getTitle(), post.getContent(),
                post.getUser().getNickname(), post.getCreatedAt(), EventType.UPDATE
        );
    }

    public static PostIndexEvent delete(Long postId) {
        return new PostIndexEvent(postId, null, null, null, null, EventType.DELETE);
    }
}
