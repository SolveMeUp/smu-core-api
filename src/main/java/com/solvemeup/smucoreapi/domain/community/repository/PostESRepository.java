package com.solvemeup.smucoreapi.domain.community.repository;

import com.solvemeup.smucoreapi.domain.community.document.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostESRepository extends ElasticsearchRepository<PostDocument, String> {
}
