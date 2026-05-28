package com.solvemeup.smucoreapi.domain.judge.execution.messaging.producer;

import com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.request.ExecutionRequestMessage;
import com.solvemeup.smucoreapi.global.messaging.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExecutionRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(ExecutionRequestMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.JUDGE_EXCHANGE,
                RabbitConfig.EXECUTION_REQUEST,
                message
        );
    }
}
