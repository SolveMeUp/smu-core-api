package com.solvemeup.smucoreapi.domain.judge.submission.messaging.producer;

import com.solvemeup.smucoreapi.domain.judge.submission.messaging.dto.request.SubmissionRequestMessage;
import com.solvemeup.smucoreapi.global.messaging.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubmissionRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(SubmissionRequestMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.SUBMISSION_EXCHANGE,
                RabbitConfig.SUBMISSION_REQUEST_ROUTING_KEY,
                message
        );
    }
}
