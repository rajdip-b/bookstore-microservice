package com.bookstorems.inventoryservice.controller;

import com.bookstorems.inventoryservice.dto.BookQuantityModifiedDTO;
import com.bookstorems.inventoryservice.service.BookService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class QueueListener {

    private final BookService bookService;

    public QueueListener(BookService bookService) {
        this.bookService = bookService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.cart-inventory.queue.add-book-to-cart}")
    public void addBookToCart(BookQuantityModifiedDTO bookQuantityModifiedDTO) {
        bookService.removeBookQuantity(bookQuantityModifiedDTO.bookId(), bookQuantityModifiedDTO.quantity());
    }

    @RabbitListener(queues = "${spring.rabbitmq.cart-inventory.queue.remove-book-from-cart}")
    public void removeBookFromCart(BookQuantityModifiedDTO bookQuantityModifiedDTO) {
        bookService.addBookQuantity(bookQuantityModifiedDTO.bookId(), bookQuantityModifiedDTO.quantity());
    }

}
