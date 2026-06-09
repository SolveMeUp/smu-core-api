package com.solvemeup.smucoreapi.domain.community.service;

import com.solvemeup.smucoreapi.domain.community.document.PostDocument;
import com.solvemeup.smucoreapi.domain.community.dto.response.PostResponse;
import com.solvemeup.smucoreapi.domain.community.entity.Post;
import com.solvemeup.smucoreapi.domain.community.repository.PostRepository;
import com.solvemeup.smucoreapi.global.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostSearchService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final PostRepository postRepository;

    public PageResponse<PostResponse> search(String keyword, Pageable pageable) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .multiMatch(mm -> mm
                                .query(keyword)
                                .fields("title^2", "content", "authorName")
                        )
                )
                .withPageable(pageable)
                .build();

        SearchHits<PostDocument> hits = elasticsearchOperations.search(query, PostDocument.class);

        List<Long> postIds = hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(PostDocument::getId)
                .map(Long::valueOf)
                .toList();

        if (postIds.isEmpty()) {
            return new PageResponse<>(
                    List.of(),
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    hits.getTotalHits(),
                    0
            );
        }

        Map<Long, Post> postsById = postRepository.findAllByIdsWithUser(postIds).stream()
                .collect(Collectors.toMap(Post::getId, Function.identity()));

        List<PostResponse> results = postIds.stream()
                .map(postsById::get)
                .filter(java.util.Objects::nonNull)
                .map(PostResponse::from)
                .toList();

        return new PageResponse<>(
                results,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                hits.getTotalHits(),
                (int) Math.ceil((double) hits.getTotalHits() / pageable.getPageSize())
        );
    }
}
