package com.solvemeup.smucoreapi.domain.community.service;

import com.solvemeup.smucoreapi.domain.community.document.PostDocument;
import com.solvemeup.smucoreapi.domain.community.dto.response.PageResponse;
import com.solvemeup.smucoreapi.domain.community.dto.response.PostSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public PageResponse<PostSearchResponse> search(String keyword, Pageable pageable) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .multiMatch(mm -> mm
                                .query(keyword)
                                .fields("title^2", "content")
                        )
                )
                .withPageable(pageable)
                .build();

        SearchHits<PostDocument> hits = elasticsearchOperations.search(query, PostDocument.class);

        List<PostSearchResponse> results = hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(PostSearchResponse::from)
                .toList();

        long totalHits = hits.getTotalHits();
        int totalPages = (int) Math.ceil((double) totalHits / pageable.getPageSize());

        return PageResponse.<PostSearchResponse>builder()
                .content(results)
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalElements(totalHits)
                .totalPages(totalPages)
                .first(pageable.getPageNumber() == 0)
                .last(pageable.getPageNumber() >= totalPages - 1)
                .build();
    }
}
