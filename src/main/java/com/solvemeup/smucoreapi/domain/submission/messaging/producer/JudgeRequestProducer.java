package com.solvemeup.smucoreapi.domain.submission.messaging.producer;

import com.solvemeup.smucoreapi.global.messaging.config.RabbitConfig;
import com.solvemeup.smucoreapi.domain.submission.messaging.dto.request.JudgeRequestMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JudgeRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(JudgeRequestMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.JUDGE_EXCHANGE,
                RabbitConfig.REQUEST_ROUTING_KEY,
                message
        );
    }
}
