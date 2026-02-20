package com.solvemeup.smucoreapi.domain.judge.submission.messaging.consumer;

import com.solvemeup.smucoreapi.domain.judge.submission.entity.SubmissionResult;
import com.solvemeup.smucoreapi.domain.judge.submission.entity.Submission;
import com.solvemeup.smucoreapi.domain.judge.submission.reader.SubmissionResultReader;
import com.solvemeup.smucoreapi.global.messaging.config.RabbitConfig;
import com.solvemeup.smucoreapi.domain.judge.submission.messaging.dto.response.SubmissionResultMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SubmissionResultConsumer {

    private final SubmissionResultReader submissionResultReader;

    @RabbitListener(queues = RabbitConfig.SUBMISSION_RESULT_QUEUE)
    @Transactional
    public void consume(SubmissionResultMessage message) {
        SubmissionResult submissionResult = submissionResultReader.getSubmissionResult(message.submissionResultId());

        if (submissionResult.getStatus().isDone()) {
            return;
        }

        submissionResult.markDone(
                message.result(),
                message.timeUsedMillis(),
                message.memoryUsedKilobytes()
        );

        Submission submission = submissionResult.getSubmission();
        submission.markDone();
    }
}
