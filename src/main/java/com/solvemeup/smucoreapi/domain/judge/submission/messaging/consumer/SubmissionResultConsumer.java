package com.solvemeup.smucoreapi.domain.judge.submission.messaging.consumer;

import com.solvemeup.smucoreapi.domain.judge.submission.messaging.dto.result.SubmissionResultMessage;
import com.solvemeup.smucoreapi.domain.judge.submission.service.SubmissionService;
import com.solvemeup.smucoreapi.global.messaging.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubmissionResultConsumer {

    private final SubmissionService submissionService;

    @RabbitListener(queues = RabbitConfig.SUBMISSION_RESULT_QUEUE)
    public void consume(SubmissionResultMessage message) {
        submissionService.applyResult(message);
    }
}
