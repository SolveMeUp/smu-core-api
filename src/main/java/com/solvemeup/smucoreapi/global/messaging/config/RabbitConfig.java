package com.solvemeup.smucoreapi.global.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String JUDGE_EXCHANGE = "judge.exchange";
    public static final String JUDGE_DLX_EXCHANGE = "judge.dlx.exchange";

    public static final String SUBMISSION_REQUEST = "judge.submission.request";
    public static final String SUBMISSION_RESULT = "judge.submission.result";
    public static final String SUBMISSION_DLQ = "judge.submission.dead";

    public static final String EXECUTION_REQUEST = "judge.execution.request";
    public static final String EXECUTION_RESULT = "judge.execution.result";
    public static final String EXECUTION_DLQ = "judge.execution.dead";

    public static final String SUBMISSION_REQUEST_QUEUE = "judge.submission.request.queue";
    public static final String SUBMISSION_RESULT_QUEUE = "judge.submission.result.queue";
    public static final String SUBMISSION_DLQ_QUEUE = "judge.submission.dead.queue";

    public static final String EXECUTION_REQUEST_QUEUE = "judge.execution.request.queue";
    public static final String EXECUTION_RESULT_QUEUE = "judge.execution.result.queue";
    public static final String EXECUTION_DLQ_QUEUE = "judge.execution.dead.queue";

    @Bean
    public TopicExchange judgeExchange() {
        return new TopicExchange(JUDGE_EXCHANGE);
    }

    @Bean
    public DirectExchange judgeDlxExchange() {
        return new DirectExchange(JUDGE_DLX_EXCHANGE);
    }

    @Bean
    public Queue submissionRequestQueue() {
        return QueueBuilder.durable(SUBMISSION_REQUEST_QUEUE)
                .withArgument("x-dead-letter-exchange", JUDGE_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", SUBMISSION_DLQ)
                .build();
    }

    @Bean
    public Binding submissionRequestBinding() {
        return BindingBuilder.bind(submissionRequestQueue())
                .to(judgeExchange())
                .with(SUBMISSION_REQUEST);
    }

    @Bean
    public Queue submissionResultQueue() {
        return QueueBuilder.durable(SUBMISSION_RESULT_QUEUE).build();
    }

    @Bean
    public Binding submissionResultBinding() {
        return BindingBuilder.bind(submissionResultQueue())
                .to(judgeExchange())
                .with(SUBMISSION_RESULT);
    }

    @Bean
    public Queue submissionDlqQueue() {
        return QueueBuilder.durable(SUBMISSION_DLQ_QUEUE).build();
    }

    @Bean
    public Binding submissionDlqBinding() {
        return BindingBuilder.bind(submissionDlqQueue())
                .to(judgeDlxExchange())
                .with(SUBMISSION_DLQ);
    }

    @Bean
    public Queue executionRequestQueue() {
        return QueueBuilder.durable(EXECUTION_REQUEST_QUEUE)
                .withArgument("x-dead-letter-exchange", JUDGE_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", EXECUTION_DLQ)
                .build();
    }

    @Bean
    public Binding executionRequestBinding() {
        return BindingBuilder.bind(executionRequestQueue())
                .to(judgeExchange())
                .with(EXECUTION_REQUEST);
    }

    @Bean
    public Queue executionResultQueue() {
        return QueueBuilder.durable(EXECUTION_RESULT_QUEUE).build();
    }

    @Bean
    public Binding executionResultBinding() {
        return BindingBuilder.bind(executionResultQueue())
                .to(judgeExchange())
                .with(EXECUTION_RESULT);
    }

    @Bean
    public Queue executionDlqQueue() {
        return QueueBuilder.durable(EXECUTION_DLQ_QUEUE).build();
    }

    @Bean
    public Binding executionDlqBinding() {
        return BindingBuilder.bind(executionDlqQueue())
                .to(judgeDlxExchange())
                .with(EXECUTION_DLQ);
    }

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
