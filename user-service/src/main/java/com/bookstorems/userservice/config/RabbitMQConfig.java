package com.bookstorems.userservice.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.user-cart.exchange}")
    private String userCartExchange;
    @Value("${spring.rabbitmq.user-cart.routing-key.delete-user}")
    private String userCartRoutingKeyDeleteUser;
    @Value("${spring.rabbitmq.user-cart.queue.delete-user}")
    private String userCartQueueDeleteUser;

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            MessageConverter messageConverter,
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(messageConverter);
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    public Declarables userCartDeclarables() {
        return new Declarables(
                new Queue(userCartQueueDeleteUser, true),
                new TopicExchange(userCartExchange),
                BindingBuilder.bind(new Queue(userCartQueueDeleteUser, true))
                        .to(new TopicExchange(userCartExchange))
                        .with(userCartRoutingKeyDeleteUser)
        );
    }

    @Bean
    public ApplicationRunner runner(ConnectionFactory cf) {
        return args -> {
            cf.createConnection();
            cf.resetConnection();
        };
    }

}
