package com.bookstorems.inventoryservice.exception;

public class BookExistsException extends RuntimeException {
    public BookExistsException() {
        super("Book already exists");
    }
}
