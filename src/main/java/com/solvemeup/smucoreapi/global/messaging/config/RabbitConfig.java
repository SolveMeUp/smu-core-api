package com.solvemeup.smucoreapi.global.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String JUDGE_EXCHANGE = "judge.exchange";
    public static final String DLX_EXCHANGE = "judge.dlx";

    public static final String REQUEST_ROUTING_KEY = "judge.request";
    public static final String RESULT_ROUTING_KEY = "judge.result";
    public static final String DLQ_ROUTING_KEY = "judge.dlq";

    public static final String REQUEST_QUEUE = "judge.request.queue";
    public static final String RESULT_QUEUE = "judge.result.queue";
    public static final String DLQ_QUEUE = "judge.dlq.queue";

    @Bean
    public DirectExchange judgeExchange() {
        return new DirectExchange(JUDGE_EXCHANGE);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE);
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
                .with(REQUEST_ROUTING_KEY);
    }

    @Bean
    public Queue resultQueue() {
        return QueueBuilder.durable(RESULT_QUEUE).build();
    }

    @Bean
    public Binding resultBinding() {
        return BindingBuilder.bind(resultQueue())
                .to(judgeExchange())
                .with(RESULT_ROUTING_KEY);
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
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
