package com.bookstorems.inventoryservice.dto;

import org.springframework.http.HttpStatus;

public record ErrorDTO(String message, HttpStatus status) {
}
