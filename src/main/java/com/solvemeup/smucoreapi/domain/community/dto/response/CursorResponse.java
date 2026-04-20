package com.solvemeup.smucoreapi.domain.community.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CursorResponse<T>(
        List<T> content,
        int size,
        boolean hasNext,
        Long lastId
) {
    public static <T> CursorResponse<T> of(List<T> content, int size, boolean hasNext, Long lastId) {
        return CursorResponse.<T>builder()
                .content(content)
                .size(size)
                .hasNext(hasNext)
                .lastId(lastId)
                .build();
    }
}
