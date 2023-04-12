package com.bookstorems.cartservice.service.impl;

import com.bookstorems.cartservice.dto.BookQuantityModifiedDTO;
import com.bookstorems.cartservice.dto.CartDTO;
import com.bookstorems.cartservice.entity.Cart;
import com.bookstorems.cartservice.proxy.BookServiceProxy;
import com.bookstorems.cartservice.repository.CartRepository;
import com.bookstorems.cartservice.service.CartService;
import com.bookstorems.cartservice.util.SecurityUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
public class CartServiceImpl implements CartService {


    @Value("${spring.rabbitmq.cart-inventory.exchange}")
    private String cartInventoryExchange;
    @Value("${spring.rabbitmq.cart-inventory.routing-key.add-book-to-cart}")
    private String cartInventoryRoutingKeyAddBookToCart;
    @Value("${spring.rabbitmq.cart-inventory.routing-key.remove-book-from-cart}")
    private String cartInventoryRoutingKeyRemoveBookFromCart;

    private final CartRepository cartRepository;
    private final BookServiceProxy bookServiceProxy;
    private final RabbitTemplate rabbitTemplate;

    public CartServiceImpl(CartRepository cartRepository, BookServiceProxy bookServiceProxy, RabbitTemplate rabbitTemplate) {
        this.cartRepository = cartRepository;
        this.bookServiceProxy = bookServiceProxy;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    @CircuitBreaker(name = "default", fallbackMethod = "addToCartFallback")
    @Transactional
    public void addToCart(Long bookId, Integer quantity) throws Exception {
        // Check if a cart for the current user already exists
        // If not, create an empty cart for the current user
        var cart = cartRepository
                .findByUserId(SecurityUtil.getUserId())
                .orElseGet(() -> {
                    var newCart = new Cart();
                    newCart.setUserId(SecurityUtil.getUserId());
                    return newCart;
                });

        var maxProductQuantity = 0;

        var req = bookServiceProxy.getBookQuantity(bookId);
        if (req.getStatusCode().is2xxSuccessful())
            maxProductQuantity = Integer.parseInt(Objects.requireNonNull(req.getBody()).toString());
        else return;

        if (maxProductQuantity < quantity) return;

        // Check if the book exists in the cart.
        // If yes, update the quantity
        // If no, add the book to the cart
        if (cart.getProducts().containsKey(bookId))
            cart.getProducts().put(bookId, cart.getProducts().get(bookId) + quantity);
        else
            cart.getProducts().put(bookId, quantity);

        notifyBookAddedToCart(bookId, quantity);

        cart = cartRepository.save(cart);

        log.info("Cart: {}", cart);
    }

    @Override
    @Transactional
    public void removeFromCart(Long bookId, Integer quantity) {
        // Get the cart of the current user.
        var cart = cartRepository.findByUserId(SecurityUtil.getUserId()).orElse(null);

        // If the cart was found, continue
        if (cart != null) {
            // Check if the book exists in the cart.
            if (cart.getProducts().containsKey(bookId)) {
                var newQuantity = cart.getProducts().get(bookId) - quantity;
                if (newQuantity > 0)
                    cart.getProducts().put(bookId, newQuantity);
                else
                    cart.getProducts().remove(bookId);

                cartRepository.save(cart);
                notifyBookRemovedFromCart(bookId, quantity);
                log.info("Cart: {}", cart);
            }
        }
    }

    @Override
    @Transactional
    public void clearCart() {
        // Get the cart of the current user.
        var cart = cartRepository.findByUserId(SecurityUtil.getUserId()).orElse(null);

        if (cart != null) {
            cart.getProducts().forEach(this::notifyBookRemovedFromCart);
            cart.getProducts().clear();

            cart = cartRepository.save(cart);
            log.info("Cart: {}", cart);
        }
    }

    @Override
    @Transactional
    public void deleteCart(Long userId) {
        var cart = cartRepository.findByUserId(userId).orElse(null);

        if (cart != null) {
            cart.getProducts().forEach(this::notifyBookRemovedFromCart);
            cartRepository.delete(cart);
            log.info("Cart deleted for user: {}", userId);
        }
    }

    @Override
    public CartDTO getCart() {
        return cartRepository
                .findByUserId(SecurityUtil.getUserId())
                .map(Cart::toDTO)
                .orElse(null);
    }

    private void notifyBookRemovedFromCart(Long bookId, Integer quantity) {
        rabbitTemplate.convertAndSend(
                cartInventoryExchange,
                cartInventoryRoutingKeyRemoveBookFromCart,
                new BookQuantityModifiedDTO(bookId, quantity)
        );
    }

    private void notifyBookAddedToCart(Long bookId, Integer quantity) {
        rabbitTemplate.convertAndSend(
                cartInventoryExchange,
                cartInventoryRoutingKeyAddBookToCart,
                new BookQuantityModifiedDTO(bookId, quantity)
        );
    }

    // Called when addToCart() fails to connect to inventory-service
    private void addToCartFallback(Long bookId, Integer quantity, Exception e) throws Exception {
        log.error("Could not add book to cart. BookId: {}, Quantity: {}", bookId, quantity, e);
        throw new RuntimeException("Internal communication error. Please try again later.");
    }

}
