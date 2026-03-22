package com.solvemeup.smucoreapi.domain.community.messaging.consumer;

import com.solvemeup.smucoreapi.domain.community.document.PostDocument;
import com.solvemeup.smucoreapi.domain.community.messaging.event.PostIndexEvent;
import com.solvemeup.smucoreapi.domain.community.repository.PostESRepository;
import com.solvemeup.smucoreapi.global.messaging.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostIndexingConsumer {

    private final PostESRepository postESRepository;

    @RabbitListener(queues = RabbitConfig.COMMUNITY_POST_QUEUE)
    public void handle(PostIndexEvent event) {
        switch (event.eventType()) {
            case INDEX, UPDATE -> postESRepository.save(
                    PostDocument.of(event.postId(), event.title(), event.content(), event.authorName(), event.createdAt())
            );
            case DELETE -> postESRepository.deleteById(String.valueOf(event.postId()));
        }
    }
}
