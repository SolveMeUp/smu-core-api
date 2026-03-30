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

    public static final String SUBMISSION_EXCHANGE = "judge.exchange";
    public static final String DLX_EXCHANGE = "judge.dlx";
    public static final String RUN_EXCHANGE = "example.exchange";
    public static final String EXAMPLE_DLX_EXCHANGE = "example.dlx";

    public static final String SUBMISSION_REQUEST_ROUTING_KEY = "judge.request";
    public static final String RESULT_ROUTING_KEY = "judge.result";
    public static final String DLQ_ROUTING_KEY = "judge.dlq";
    public static final String RUN_ROUTING_KEY = "example.run";
    public static final String EXAMPLE_RESULT_ROUTING_KEY = "example.result";
    public static final String EXAMPLE_DLQ_ROUTING_KEY = "example.dlq";

    public static final String REQUEST_QUEUE = "judge.request.queue";
    public static final String SUBMISSION_RESULT_QUEUE = "judge.result.queue";
    public static final String DLQ_QUEUE = "judge.dlq.queue";
    public static final String EXAMPLE_RUN_QUEUE = "example.run.queue";
    public static final String RUN_RESULT_QUEUE = "example.result.queue";
    public static final String EXAMPLE_DLQ_QUEUE = "example.dlq.queue";

    @Bean
    public DirectExchange judgeExchange() {
        return new DirectExchange(SUBMISSION_EXCHANGE);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }

    @Bean
    public DirectExchange exampleExchange() {
        return new DirectExchange(RUN_EXCHANGE);
    }

    @Bean
    public DirectExchange exampleDlxExchange() {
        return new DirectExchange(EXAMPLE_DLX_EXCHANGE);
    }

    @Bean
    public Queue requestQueue() {
        return QueueBuilder.durable(REQUEST_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding requestBinding() {
        return BindingBuilder.bind(requestQueue())
                .to(judgeExchange())
                .with(SUBMISSION_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Queue exampleRunQueue() {
        return QueueBuilder.durable(EXAMPLE_RUN_QUEUE)
                .withArgument("x-dead-letter-exchange", EXAMPLE_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", EXAMPLE_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding exampleRunBinding() {
        return BindingBuilder.bind(exampleRunQueue())
                .to(exampleExchange())
                .with(RUN_ROUTING_KEY);
    }

    @Bean
    public Queue resultQueue() {
        return QueueBuilder.durable(SUBMISSION_RESULT_QUEUE).build();
    }

    @Bean
    public Binding resultBinding() {
        return BindingBuilder.bind(resultQueue())
                .to(judgeExchange())
                .with(RESULT_ROUTING_KEY);
    }

    @Bean
    public Queue exampleResultQueue() {
        return QueueBuilder.durable(RUN_RESULT_QUEUE).build();
    }

    @Bean
    public Binding exampleResultBinding() {
        return BindingBuilder.bind(exampleResultQueue())
                .to(exampleExchange())
                .with(EXAMPLE_RESULT_ROUTING_KEY);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ_QUEUE).build();
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(dlxExchange())
                .with(DLQ_ROUTING_KEY);
    }

    @Bean
    public Queue exampleDeadLetterQueue() {
        return QueueBuilder.durable(EXAMPLE_DLQ_QUEUE).build();
    }

    @Bean
    public Binding exampleDlqBinding() {
        return BindingBuilder.bind(exampleDeadLetterQueue())
                .to(exampleDlxExchange())
                .with(EXAMPLE_DLQ_ROUTING_KEY);
    }

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
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
