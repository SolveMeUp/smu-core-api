package com.solvemeup.smucoreapi.domain.judge.execution.messaging.consumer;

import com.solvemeup.smucoreapi.domain.judge.execution.entity.Execution;
import com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionResult;
import com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionResultStatus;
import com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.response.ExecutionResultMessage;
import com.solvemeup.smucoreapi.domain.judge.execution.repository.ExecutionRepository;
import com.solvemeup.smucoreapi.domain.judge.execution.repository.ExecutionResultRepository;
import com.solvemeup.smucoreapi.global.messaging.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ExecutionResultConsumer {

    private final ExecutionRepository executionRepository;
    private final ExecutionResultRepository executionResultRepository;

    @RabbitListener(queues = RabbitConfig.RUN_RESULT_QUEUE)
    @Transactional
    public void consume(ExecutionResultMessage message) {

        Execution execution = executionRepository.findById(message.executionId())
                .orElse(null);

        if (execution == null) {
            return;
        }

        if (executionResultRepository.existsByExecutionIdAndCaseIndex(
                message.executionId(),
                message.caseIndex())) {
            return;
        }

        ExecutionResult result = ExecutionResult.create(
                execution,
                message.caseIndex(),
                ExecutionResultStatus.valueOf(message.status()),
                message.expectedOutput(),
                message.actualOutput(),
                message.timeUsedMillis(),
                message.memoryUsedKilobytes()
        );

        try {
            executionResultRepository.save(result);
        } catch (DataIntegrityViolationException e) {
            return;
        }

        long completedCount = executionResultRepository.countByExecutionId(message.executionId());

        if (completedCount >= execution.getTotalCaseCount()) {
            execution.markDone();
        }
    }
}
