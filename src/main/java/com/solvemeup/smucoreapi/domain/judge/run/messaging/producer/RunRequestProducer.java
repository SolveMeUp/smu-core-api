package com.solvemeup.smucoreapi.domain.judge.run.messaging.producer;

import com.solvemeup.smucoreapi.domain.judge.run.messaging.dto.request.RunMessage;
import com.solvemeup.smucoreapi.global.messaging.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RunRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(RunMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.RUN_EXCHANGE,
                RabbitConfig.RUN_ROUTING_KEY,
                message
        );
    }
}
