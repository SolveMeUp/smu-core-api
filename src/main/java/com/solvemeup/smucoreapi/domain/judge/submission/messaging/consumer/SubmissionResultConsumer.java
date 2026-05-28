package com.solvemeup.smucoreapi.domain.judge.submission.messaging.consumer;

import com.solvemeup.smucoreapi.domain.judge.submission.entity.SubmissionCaseResult;
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
        SubmissionCaseResult submissionCaseResult = submissionResultReader.getSubmissionResult(message.submissionResultId());

        if (submissionCaseResult.getStatus().isDone()) {
            return;
        }

        submissionCaseResult.markDone(
                message.result(),
                message.timeUsedMillis(),
                message.memoryUsedKilobytes()
        );

        Submission submission = submissionCaseResult.getSubmission();
        submission.markDone();
    }
}
