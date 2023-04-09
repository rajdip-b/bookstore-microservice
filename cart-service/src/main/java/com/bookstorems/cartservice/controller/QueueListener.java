package com.bookstorems.cartservice.controller;

import com.bookstorems.cartservice.service.CartService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class QueueListener {

    private final CartService cartService;

    public QueueListener(CartService cartService) {
        this.cartService = cartService;
    }

    @RabbitListener(queues = {"${spring.rabbitmq.user-cart.queue.delete-user}"})
    public void deleteUser(Long userId) {
        cartService.deleteCart(userId);
    }

}
