package com.solvemeup.smucoreapi.domain.community.dto.response;

import com.solvemeup.smucoreapi.domain.community.document.PostDocument;

import java.time.LocalDateTime;

public record PostSearchResponse(
        String id,
        String title,
        String content,
        String authorName,
        LocalDateTime createdAt
) {
    public static PostSearchResponse from(PostDocument doc) {
        return new PostSearchResponse(
                doc.getId(),
                doc.getTitle(),
                doc.getContent(),
                doc.getAuthorName(),
                doc.getCreatedAt()
        );
    }
}
