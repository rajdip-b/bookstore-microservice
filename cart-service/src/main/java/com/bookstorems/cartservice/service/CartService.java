package com.bookstorems.cartservice.service;

import com.bookstorems.cartservice.dto.CartDTO;

public interface CartService {

    /**
     * Add a book to the cart
     *
     * @param bookId The id of the book to add
     * @param quantity The quantity of the book to add
     * @throws Exception Throws an exception if the book is not found or the quantity is not valid
     */
    void addToCart(Long bookId, Integer quantity) throws Exception;

    /**
     * Remove a book from the cart with the desired quantity
     *
     * @param bookId The id of the book to remove
     * @param quantity The quantity of the book to remove
     */
    void removeFromCart(Long bookId, Integer quantity);

    /**
     * Remove all the books from the cart
     */
    void clearCart();

    /**
     * Delete the cart of the user. This method is called when the user is deleted.
     * The RabbitMQ listener calls this method
     *
     * @param userId The id of the user
     */
    void deleteCart(Long userId);

    /**
     * Get the details of the cart of the user
     *
     * @return The cart details
     */
    CartDTO getCart();

}
