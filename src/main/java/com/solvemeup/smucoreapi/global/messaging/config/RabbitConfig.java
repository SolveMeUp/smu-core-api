package com.solvemeup.smucoreapi.global.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String COMMUNITY_EXCHANGE = "community.exchange";
    public static final String COMMUNITY_DLX_EXCHANGE = "community.dlx";
    public static final String COMMUNITY_POST_ROUTING_KEY = "community.post.index";
    public static final String COMMUNITY_DLQ_ROUTING_KEY = "community.post.dlq";
    public static final String COMMUNITY_POST_QUEUE = "community.post.index.queue";
    public static final String COMMUNITY_DLQ_QUEUE = "community.post.dlq.queue";

    public static final String EXECUTION_EXCHANGE = "execution.exchange";
    public static final String EXECUTION_DLX_EXCHANGE = "execution.dlx";
    public static final String EXECUTION_REQUEST_ROUTING_KEY = "execution.request";
    public static final String EXECUTION_RESULT_ROUTING_KEY = "execution.result";
    public static final String EXECUTION_DLQ_ROUTING_KEY = "execution.dlq";
    public static final String EXECUTION_REQUEST_QUEUE = "execution.request.queue";
    public static final String EXECUTION_RESULT_QUEUE = "execution.result.queue";
    public static final String EXECUTION_DLQ_QUEUE = "execution.dlq.queue";

    public static final String SUBMISSION_EXCHANGE = "submission.exchange";
    public static final String SUBMISSION_DLX_EXCHANGE = "submission.dlx";
    public static final String SUBMISSION_REQUEST_ROUTING_KEY = "submission.request";
    public static final String SUBMISSION_RESULT_ROUTING_KEY = "submission.result";
    public static final String SUBMISSION_DLQ_ROUTING_KEY = "submission.dlq";
    public static final String SUBMISSION_REQUEST_QUEUE = "submission.request.queue";
    public static final String SUBMISSION_RESULT_QUEUE = "submission.result.queue";
    public static final String SUBMISSION_DLQ_QUEUE = "submission.dlq.queue";

    @Bean
    public DirectExchange communityExchange() {
        return new DirectExchange(COMMUNITY_EXCHANGE);
    }

    @Bean
    public DirectExchange communityDlxExchange() {
        return new DirectExchange(COMMUNITY_DLX_EXCHANGE);
    }

    @Bean
    public Queue communityPostQueue() {
        return QueueBuilder.durable(COMMUNITY_POST_QUEUE)
                .withArgument("x-dead-letter-exchange", COMMUNITY_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", COMMUNITY_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding communityPostBinding() {
        return BindingBuilder.bind(communityPostQueue())
                .to(communityExchange())
                .with(COMMUNITY_POST_ROUTING_KEY);
    }

    @Bean
    public Queue communityDlqQueue() {
        return QueueBuilder.durable(COMMUNITY_DLQ_QUEUE).build();
    }

    @Bean
    public Binding communityDlqBinding() {
        return BindingBuilder.bind(communityDlqQueue())
                .to(communityDlxExchange())
                .with(COMMUNITY_DLQ_ROUTING_KEY);
    }

    @Bean
    public DirectExchange executionExchange() {
        return new DirectExchange(EXECUTION_EXCHANGE);
    }

    @Bean
    public DirectExchange executionDlxExchange() {
        return new DirectExchange(EXECUTION_DLX_EXCHANGE);
    }

    @Bean
    public Queue executionRequestQueue() {
        return QueueBuilder.durable(EXECUTION_REQUEST_QUEUE)
                .withArgument("x-dead-letter-exchange", EXECUTION_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", EXECUTION_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding executionRequestBinding() {
        return BindingBuilder.bind(executionRequestQueue())
                .to(executionExchange())
                .with(EXECUTION_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Queue executionResultQueue() {
        return QueueBuilder.durable(EXECUTION_RESULT_QUEUE).build();
    }

    @Bean
    public Binding executionResultBinding() {
        return BindingBuilder.bind(executionResultQueue())
                .to(executionExchange())
                .with(EXECUTION_RESULT_ROUTING_KEY);
    }

    @Bean
    public Queue executionDeadLetterQueue() {
        return QueueBuilder.durable(EXECUTION_DLQ_QUEUE).build();
    }

    @Bean
    public Binding executionDlqBinding() {
        return BindingBuilder.bind(executionDeadLetterQueue())
                .to(executionDlxExchange())
                .with(EXECUTION_DLQ_ROUTING_KEY);
    }

    @Bean
    public DirectExchange submissionExchange() {
        return new DirectExchange(SUBMISSION_EXCHANGE);
    }

    @Bean
    public DirectExchange submissionDlxExchange() {
        return new DirectExchange(SUBMISSION_DLX_EXCHANGE);
    }

    @Bean
    public Queue submissionRequestQueue() {
        return QueueBuilder.durable(SUBMISSION_REQUEST_QUEUE)
                .withArgument("x-dead-letter-exchange", SUBMISSION_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", SUBMISSION_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding submissionRequestBinding() {
        return BindingBuilder.bind(submissionRequestQueue())
                .to(submissionExchange())
                .with(SUBMISSION_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Queue submissionResultQueue() {
        return QueueBuilder.durable(SUBMISSION_RESULT_QUEUE).build();
    }

    @Bean
    public Binding submissionResultBinding() {
        return BindingBuilder.bind(submissionResultQueue())
                .to(submissionExchange())
                .with(SUBMISSION_RESULT_ROUTING_KEY);
    }

    @Bean
    public Queue submissionDeadLetterQueue() {
        return QueueBuilder.durable(SUBMISSION_DLQ_QUEUE).build();
    }

    @Bean
    public Binding submissionDlqBinding() {
        return BindingBuilder.bind(submissionDeadLetterQueue())
                .to(submissionDlxExchange())
                .with(SUBMISSION_DLQ_ROUTING_KEY);
    }

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
