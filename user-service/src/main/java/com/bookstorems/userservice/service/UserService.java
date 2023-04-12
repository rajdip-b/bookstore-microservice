package com.bookstorems.userservice.service;

import com.bookstorems.userservice.dto.UserDTO;
import com.bookstorems.userservice.exception.UserExistsException;
import com.bookstorems.userservice.exception.UserNotFoundException;
import org.springframework.data.domain.Page;

/**
 * This class is responsible for laying out the contract for the user service
 */
public interface UserService {

    /**
     * Logs in an already existing user
     * @param userDTO - the user to be logged in
     * @return - the token for the user
     * @throws UserNotFoundException - if the user is not found
     */
    String login(UserDTO userDTO) throws UserNotFoundException;

    /**
     * Signs up a new user
     * @param userDTO - the user to be signed up
     * @return - the token for the user
     * @throws UserExistsException - if the user already exists
     */
    String signup(UserDTO userDTO) throws UserExistsException;

    /**
     * Creates a new user
     * @param userDTO - the user to be created
     * @throws UserExistsException - if the user already exists
     */
    void createUser(UserDTO userDTO) throws UserExistsException;

    /**
     * Updates the currently logged-in user
     * @param userDTO - the user to be updated
     * @return - the updated user
     * @throws UserNotFoundException - if the user is not found
     */
    String updateUser(UserDTO userDTO);

    /**
     * Deletes the currently logged-in user
     */
    void deleteUser();

    /**
     * Deletes a user
     * @param userId - the id of the user to be deleted
     * @throws UserNotFoundException - if the user is not found
     */
    void deleteUser(Long userId);

    /**
     * Updates a user
     * @param userId - the id of the user to be updated
     * @param userDTO - the user to be updated
     * @throws UserNotFoundException - if the user is not found
     */
    void updateUser(Long userId, UserDTO userDTO) throws UserNotFoundException;

    /**
     * Gets all users
     * @param page - the page number
     * @param size - the size of the page
     * @return - the page of users
     */
    Page<UserDTO> getAllUsers(int page, int size);

    /**
     * Get details about the currently logged-in user
     * @return - the user
     * @throws UserNotFoundException - if the user is not found
     */
    UserDTO getSelf();

    /**
     * Gets a user
     * @param userId - the id of the user to be retrieved
     * @return - the user
     * @throws UserNotFoundException - if the user is not found
     */
    UserDTO getUser(Long userId) throws UserNotFoundException;

}
