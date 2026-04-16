package com.solvemeup.smucoreapi.domain.community.viewcount;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 게시글 조회수를 Redis에 누적하고, 주기적으로 DB에 반영하기 위한 버퍼.
 *
 * <p>구조:
 * <ul>
 *   <li>{@code post:view:count:{postId}} - 게시글별 누적 조회수 (INCR)</li>
 *   <li>{@code post:view:dirty} - 플러시가 필요한 postId 집합 (SET)</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class ViewCountStorage {

    private static final String COUNT_KEY_PREFIX = "post:view:count:";
    private static final String DIRTY_KEY = "post:view:dirty";

    private final StringRedisTemplate redisTemplate;

    /**
     * 조회수 증가. 누적된 총 delta를 반환한다.
     */
    public long increment(Long postId) {
        Long current = redisTemplate.opsForValue().increment(COUNT_KEY_PREFIX + postId);
        redisTemplate.opsForSet().add(DIRTY_KEY, postId.toString());
        return current != null ? current : 0L;
    }

    /**
     * 누적된 조회수 delta를 모두 꺼내고 Redis 카운터를 0으로 리셋한다.
     */
    public Map<Long, Long> drainAll() {
        Set<String> dirtyIds = redisTemplate.opsForSet().members(DIRTY_KEY);
        if (dirtyIds == null || dirtyIds.isEmpty()) {
            return Map.of();
        }
        redisTemplate.delete(DIRTY_KEY);

        Map<Long, Long> result = new HashMap<>();
        for (String idStr : dirtyIds) {
            Long postId = Long.parseLong(idStr);
            String oldValue = redisTemplate.opsForValue().getAndSet(COUNT_KEY_PREFIX + postId, "0");
            if (oldValue == null) continue;

            long delta = Long.parseLong(oldValue);
            if (delta > 0) {
                result.put(postId, delta);
            }
        }
        return result;
    }
}
