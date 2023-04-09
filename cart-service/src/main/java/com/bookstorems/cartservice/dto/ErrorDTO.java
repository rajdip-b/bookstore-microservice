package com.bookstorems.cartservice.dto;

import org.springframework.http.HttpStatus;

public record ErrorDTO(String message, HttpStatus status) {
}
