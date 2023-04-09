package com.bookstorems.cartservice.config;

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

    @Value("${spring.rabbitmq.cart-inventory.exchange}")
    private String cartInventoryExchange;
    @Value("${spring.rabbitmq.cart-inventory.routing-key.add-book-to-cart}")
    private String cartInventoryRoutingKeyAddBookToCart;
    @Value("${spring.rabbitmq.cart-inventory.routing-key.remove-book-from-cart}")
    private String cartInventoryRoutingKeyRemoveBookFromCart;
    @Value("${spring.rabbitmq.cart-inventory.queue.add-book-to-cart}")
    private String cartInventoryQueueAddBookToCart;
    @Value("${spring.rabbitmq.cart-inventory.queue.remove-book-from-cart}")
    private String cartInventoryQueueRemoveBookFromCart;

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
    public Declarables cartInventoryDeclarables() {
        var addBookToCartQueue = new Queue(cartInventoryQueueAddBookToCart, true);
        var removeBookFromCartQueue = new Queue(cartInventoryQueueRemoveBookFromCart, true);
        var cartInventoryExchange = new TopicExchange(this.cartInventoryExchange);
        return new Declarables(
                addBookToCartQueue,
                removeBookFromCartQueue,
                cartInventoryExchange,
                BindingBuilder.bind(addBookToCartQueue).to(cartInventoryExchange).with(cartInventoryRoutingKeyAddBookToCart),
                BindingBuilder.bind(removeBookFromCartQueue).to(cartInventoryExchange).with(cartInventoryRoutingKeyRemoveBookFromCart)
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
