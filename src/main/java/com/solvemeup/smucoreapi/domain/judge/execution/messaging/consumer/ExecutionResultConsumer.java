package com.solvemeup.smucoreapi.domain.judge.execution.messaging.consumer;

import com.solvemeup.smucoreapi.domain.judge.execution.entity.Execution;
import com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionCaseResult;
import com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.response.ExecutionResultMessage;
import com.solvemeup.smucoreapi.domain.judge.execution.reader.ExecutionReader;
import com.solvemeup.smucoreapi.domain.judge.execution.repository.ExecutionCaseResultRepository;
import com.solvemeup.smucoreapi.global.messaging.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionCaseResultStatus.*;
import static com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionStatus.*;

@Component
@RequiredArgsConstructor
public class ExecutionResultConsumer {

    private final ExecutionReader executionReader;
    private final ExecutionCaseResultRepository executionCaseResultRepository;

    @Transactional
    @RabbitListener(queues = RabbitConfig.EXECUTION_RESULT_QUEUE)
    public void consume(ExecutionResultMessage message) {
        Execution execution = executionReader.getExecution(message.executionId());

        if (executionCaseResultRepository.existsByExecutionIdAndCaseIndex(message.executionId(), message.caseIndex())) {
            return;
        }

        if (message.status() == SYSTEM_ERROR) {
            execution.markError();
            return;
        }

        if (execution.getStatus() == PENDING) {
            execution.markRunning();
        }

        ExecutionCaseResult result = ExecutionCaseResult.create(
                execution,
                message.caseIndex(),
                message.status(),
                message.actualOutput(),
                message.timeUsedMillis(),
                message.memoryUsedKilobytes()
        );
        executionCaseResultRepository.save(result);

        long completedCount = executionCaseResultRepository.countByExecutionId(message.executionId());

        if (completedCount == execution.getTotalCaseCount()) {
            execution.markDone();
        }
    }
}
