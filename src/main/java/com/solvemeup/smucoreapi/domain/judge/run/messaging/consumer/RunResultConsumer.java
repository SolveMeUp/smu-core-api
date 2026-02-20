package com.solvemeup.smucoreapi.domain.judge.run.messaging.consumer;

import com.solvemeup.smucoreapi.domain.judge.run.entity.Run;
import com.solvemeup.smucoreapi.domain.judge.run.entity.RunResult;
import com.solvemeup.smucoreapi.domain.judge.run.entity.RunResultStatus;
import com.solvemeup.smucoreapi.domain.judge.run.messaging.dto.response.ExecutionResultMessage;
import com.solvemeup.smucoreapi.domain.judge.run.repository.RunRepository;
import com.solvemeup.smucoreapi.domain.judge.run.repository.RunResultRepository;
import com.solvemeup.smucoreapi.global.messaging.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RunResultConsumer {

    private final RunRepository runRepository;
    private final RunResultRepository runResultRepository;

    @RabbitListener(queues = RabbitConfig.RUN_RESULT_QUEUE)
    @Transactional
    public void consume(ExecutionResultMessage message) {

        Run run = runRepository.findById(message.executionId())
                .orElse(null);

        if (run == null) {
            return;
        }

        if (runResultRepository.existsByRunIdAndCaseIndex(
                message.executionId(),
                message.caseIndex())) {
            return;
        }

        RunResult result = RunResult.create(
                run,
                message.caseIndex(),
                RunResultStatus.valueOf(message.status()),
                message.expectedOutput(),
                message.actualOutput(),
                message.timeUsedMillis(),
                message.memoryUsedKilobytes()
        );

        try {
            runResultRepository.save(result);
        } catch (DataIntegrityViolationException e) {
            return;
        }

        long completedCount = runResultRepository.countByRunId(message.executionId());

        if (completedCount >= run.getTotalCaseCount()) {
            run.markDone();
        }
    }
}
