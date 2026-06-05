package com.solvemeup.smucoreapi.domain.judge.execution.messaging.consumer;

import com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.result.ExecutionResultMessage;
import com.solvemeup.smucoreapi.domain.judge.execution.service.ExecutionService;
import com.solvemeup.smucoreapi.global.messaging.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExecutionResultConsumer {

    private final ExecutionService executionService;

    @RabbitListener(queues = RabbitConfig.EXECUTION_RESULT_QUEUE)
    public void consume(ExecutionResultMessage message) {
        executionService.applyResult(message);
    }
}
