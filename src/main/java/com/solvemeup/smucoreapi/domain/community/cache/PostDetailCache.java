package com.solvemeup.smucoreapi.domain.community.cache;

import com.solvemeup.smucoreapi.global.cache.CacheConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PostDetailCache {

    private final CacheManager cacheManager;

    public void evictAfterCommit(Long postId) {
        evictAfterCommit(List.of(postId));
    }

    public void evictAfterCommit(Collection<Long> postIds) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            evict(postIds);
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                evict(postIds);
            }
        });
    }

    private void evict(Collection<Long> postIds) {
        Cache cache = cacheManager.getCache(CacheConfig.POST_DETAIL);
        if (cache == null) {
            return;
        }
        postIds.forEach(cache::evict);
    }
}
