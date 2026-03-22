package com.solvemeup.smucoreapi.domain.community.messaging.publisher;

import com.solvemeup.smucoreapi.domain.community.messaging.event.PostIndexEvent;
import com.solvemeup.smucoreapi.global.messaging.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PostEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PostIndexEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.COMMUNITY_EXCHANGE,
                RabbitConfig.COMMUNITY_POST_ROUTING_KEY,
                event
        );
    }
}
