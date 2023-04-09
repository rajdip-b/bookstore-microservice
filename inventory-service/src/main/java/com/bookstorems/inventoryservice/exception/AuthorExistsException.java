package com.bookstorems.inventoryservice.exception;

public class AuthorExistsException extends RuntimeException {
    public AuthorExistsException() {
        super("Author already exists");
    }
}
