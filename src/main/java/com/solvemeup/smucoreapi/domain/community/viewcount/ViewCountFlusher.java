package com.solvemeup.smucoreapi.domain.community.viewcount;

import com.solvemeup.smucoreapi.domain.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Redis에 누적된 조회수 delta를 주기적으로 DB에 반영한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ViewCountFlusher {

    private final ViewCountStorage storage;
    private final PostRepository postRepository;

    @Scheduled(fixedRate = 10_000)
    @Transactional
    public void flush() {
        Map<Long, Long> deltas = storage.drainAll();
        if (deltas.isEmpty()) return;

        deltas.forEach(postRepository::incrementViewCount);
        log.debug("Flushed view counts for {} posts", deltas.size());
    }
}
