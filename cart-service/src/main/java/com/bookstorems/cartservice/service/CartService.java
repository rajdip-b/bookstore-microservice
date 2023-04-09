package com.bookstorems.cartservice.service;

import com.bookstorems.cartservice.dto.CartDTO;

public interface CartService {

    void addToCart(Long bookId, Integer quantity) throws Exception;
    void removeFromCart(Long bookId, Integer quantity);
    void clearCart();
    void deleteCart(Long userId);
    CartDTO getCart();

}
