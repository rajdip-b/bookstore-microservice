package com.bookstorems.userservice.service;

import com.bookstorems.userservice.dto.UserDTO;
import com.bookstorems.userservice.exception.UserExistsException;
import com.bookstorems.userservice.exception.UserNotFoundException;
import org.springframework.data.domain.Page;

public interface UserService {

    String login(UserDTO userDTO) throws UserNotFoundException;
    String signup(UserDTO userDTO) throws UserExistsException;
    void createUser(UserDTO userDTO) throws UserExistsException;
    String updateUser(UserDTO userDTO);
    void deleteUser();
    void deleteUser(Long userId);
    void updateUser(Long userId, UserDTO userDTO) throws UserNotFoundException;
    Page<UserDTO> getAllUsers(int page, int size);
    UserDTO getSelf();
    UserDTO getUser(Long userId) throws UserNotFoundException;

}
