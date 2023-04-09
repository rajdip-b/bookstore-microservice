package com.bookstorems.cartservice.controller;

import com.bookstorems.cartservice.dto.ErrorDTO;
import com.bookstorems.cartservice.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/cart")
@Slf4j
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PutMapping("/add/{bookId}/{quantity}")
    public ResponseEntity<?> addToCart(@PathVariable Long bookId, @PathVariable Integer quantity) {
        try {
            cartService.addToCart(bookId, quantity);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error while adding book to cart: ", e);
            return ResponseEntity.internalServerError().body(new ErrorDTO(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @DeleteMapping("/remove/{bookId}/{quantity}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long bookId, @PathVariable Integer quantity) {
        try {
            cartService.removeFromCart(bookId, quantity);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error while removing book from cart: ", e);
            return ResponseEntity.internalServerError().body(new ErrorDTO(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart() {
        try {
            cartService.clearCart();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error while clearing cart: ", e);
            return ResponseEntity.internalServerError().body(new ErrorDTO(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getCart() {
        try {
            return ResponseEntity.ok(cartService.getCart());
        } catch (Exception e) {
            log.error("Error while getting cart: ", e);
            return ResponseEntity.internalServerError().body(new ErrorDTO(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
