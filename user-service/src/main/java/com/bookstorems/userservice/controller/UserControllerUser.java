package com.bookstorems.userservice.controller;

import com.bookstorems.userservice.dto.ErrorDTO;
import com.bookstorems.userservice.dto.UserDTO;
import com.bookstorems.userservice.exception.UserNotFoundException;
import com.bookstorems.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserControllerUser {

    private final UserService userService;

    public UserControllerUser(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/")
    public ResponseEntity<?> update(@RequestBody UserDTO userDTO) {
        try {
            return ResponseEntity.ok(userService.updateUser(userDTO));
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(new ErrorDTO(e.getMessage(), HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(new ErrorDTO(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @DeleteMapping("/")
    public ResponseEntity<?> delete() {
        try {
            userService.deleteUser();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(new ErrorDTO(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getSelf() {
        try {
            return ResponseEntity.ok(userService.getSelf());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(new ErrorDTO(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
