package com.bookstorems.userservice.exception;

public class UserExistsException extends RuntimeException {
    public UserExistsException() {
        super("The user with the email already exists!");
    }
}
