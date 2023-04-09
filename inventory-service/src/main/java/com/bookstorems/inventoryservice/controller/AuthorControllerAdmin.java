package com.bookstorems.inventoryservice.controller;

import com.bookstorems.inventoryservice.dto.AuthorDTO;
import com.bookstorems.inventoryservice.dto.ErrorDTO;
import com.bookstorems.inventoryservice.exception.AuthorExistsException;
import com.bookstorems.inventoryservice.exception.AuthorNotFoundException;
import com.bookstorems.inventoryservice.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/author")
@Slf4j
public class AuthorControllerAdmin {

    private final AuthorService authorService;

    public AuthorControllerAdmin(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createAuthor(@RequestBody AuthorDTO authorDTO) {
        try {
            return ResponseEntity.ok(authorService.createAuthor(authorDTO));
        } catch (AuthorExistsException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(new ErrorDTO(e.getMessage(), HttpStatus.CONFLICT));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(new ErrorDTO(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PutMapping("/{authorId}")
    public ResponseEntity<?> updateAuthor(@PathVariable Long authorId, @RequestBody AuthorDTO authorDTO) {
        try {
            authorService.updateAuthor(authorId, authorDTO);
            return ResponseEntity.ok().build();
        } catch (AuthorExistsException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(new ErrorDTO(e.getMessage(), HttpStatus.CONFLICT));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(new ErrorDTO(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<?> deleteAuthor(@PathVariable Long authorId) {
        try {
            authorService.deleteAuthor(authorId);
            return ResponseEntity.ok().build();
        } catch (AuthorNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(new ErrorDTO(e.getMessage(), HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(new ErrorDTO(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
