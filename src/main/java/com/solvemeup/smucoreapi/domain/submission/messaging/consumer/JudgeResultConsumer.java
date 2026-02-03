package com.solvemeup.smucoreapi.domain.submission.messaging.consumer;

import com.solvemeup.smucoreapi.domain.submission.entity.Judge;
import com.solvemeup.smucoreapi.domain.submission.entity.Submission;
import com.solvemeup.smucoreapi.domain.submission.reader.JudgeReader;
import com.solvemeup.smucoreapi.global.messaging.config.RabbitConfig;
import com.solvemeup.smucoreapi.domain.submission.messaging.dto.response.JudgeResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class JudgeResultConsumer {

    private final JudgeReader judgeReader;

    @RabbitListener(queues = RabbitConfig.RESULT_QUEUE)
    @Transactional
    public void consume(JudgeResponseMessage message) {
        Judge judge = judgeReader.getJudge(message.judgeId());

        if (judge.getStatus().isDone()) {
            return;
        }

        judge.markDone(
                message.result(),
                message.timeUsedMillis(),
                message.memoryUsedMegabytes()
        );

        Submission submission = judge.getSubmission();
        submission.markDone();
    }
}
