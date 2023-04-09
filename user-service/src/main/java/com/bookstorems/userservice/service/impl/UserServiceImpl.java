package com.bookstorems.userservice.service.impl;

import com.bookstorems.userservice.dto.UserDTO;
import com.bookstorems.userservice.entity.User;
import com.bookstorems.userservice.exception.UserExistsException;
import com.bookstorems.userservice.exception.UserNotFoundException;
import com.bookstorems.userservice.repository.UserRepository;
import com.bookstorems.userservice.security.JWTTokenUtil;
import com.bookstorems.userservice.service.UserService;
import com.bookstorems.userservice.util.SecurityUtil;
import com.bookstorems.userservice.util.SequenceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${spring.rabbitmq.user-cart.exchange}")
    private String userCartExchange;
    @Value("${spring.rabbitmq.user-cart.routing-key.delete-user}")
    private String userCartRoutingKeyDeleteUser;

    private final UserRepository userRepository;
    private final JWTTokenUtil jwtTokenUtil;
    private final SequenceGenerator sequenceGenerator;
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;

    public UserServiceImpl(
            UserRepository userRepository,
            JWTTokenUtil jwtTokenUtil,
            SequenceGenerator sequenceGenerator,
            PasswordEncoder passwordEncoder,
            RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.sequenceGenerator = sequenceGenerator;
        this.passwordEncoder = passwordEncoder;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public String login(UserDTO userDTO) throws UserNotFoundException {
        // Get the user from the database
        var user = userRepository.findByEmail(userDTO.email())
                .orElseThrow(UserNotFoundException::new);

        // Check if the password is correct
        if (!passwordEncoder.matches(userDTO.password(), user.getPassword()))
            throw new UserNotFoundException();

        // Return jwt token
        return jwtTokenUtil.generateToken(user);
    }

    @Override
    @Transactional
    public String signup(UserDTO userDTO) throws UserExistsException {
        // Check if a user with same email already exists
        if (userRepository.existsByEmail(userDTO.email()))
            throw new UserExistsException();

        // Create the user
        var user = saveUser(userDTO);
        log.info("User created: {}", user);

        // Return jwt token
        return jwtTokenUtil.generateToken(user);
    }

    @Override
    @Transactional
    public void createUser(UserDTO userDTO) throws UserExistsException {
        // Check if a user with same email already exists
        if (userRepository.existsByEmail(userDTO.email()))
            throw new UserExistsException();

        // Create the user
        var user = saveUser(userDTO);
        log.info("User created: {}", user);
    }

    @Override
    @Transactional
    public String updateUser(UserDTO userDTO) {
        // Get the id of the user from security context
        var id = SecurityUtil.getUserId();

        // Get the user from the database
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        // Update the user
        user = updateUser(user, userDTO);

        // Return a new jwt in case the password was modified
        return jwtTokenUtil.generateToken(user);
    }

    @Override
    @Transactional
    public void deleteUser() {
        // Get the id of the user from security context
        var id = SecurityUtil.getUserId();

        // Delete the user
        userRepository.deleteById(id);
        log.info("User deleted: {}", id);

        notifyUserDeleted(id);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        // Delete the user
        userRepository.deleteById(userId);
        log.info("User deleted: {}", userId);

        notifyUserDeleted(userId);
    }

    @Override
    @Transactional
    public void updateUser(Long userId, UserDTO userDTO) throws UserNotFoundException {
        // Get the user from the database
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // Update the user
        user = updateUser(user, userDTO);
        log.info("User updated: {}", user);
    }

    @Override
    public Page<UserDTO> getAllUsers(int page, int size) {
        return userRepository
                .findAll(PageRequest.of(page, size))
                .map(User::toDTO);
    }

    @Override
    public UserDTO getSelf() {
        // Get the id of the user from security context
        var id = SecurityUtil.getUserId();

        // Get the user from the database
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        // Return the user
        return user.toDTO();
    }

    @Override
    public UserDTO getUser(Long userId) throws UserNotFoundException {
        // Get the user from the database
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // Return the user
        return user.toDTO();
    }

    private User saveUser(UserDTO userDTO) {
        var user = new User();
        user.setId(sequenceGenerator.generateSequence(User.SEQUENCE_NAME));
        user.setEmail(userDTO.email());
        user.setName(userDTO.name());
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        return userRepository.save(user);
    }

    private User updateUser(User user, UserDTO userDTO) {
        user.setName(userDTO.name() != null ? userDTO.name() : user.getName());
        user.setPassword(userDTO.password() != null ? passwordEncoder.encode(userDTO.password()) : user.getPassword());
        return userRepository.save(user);
    }

    private void notifyUserDeleted(Long userId) {
        rabbitTemplate.convertAndSend(
                userCartExchange,
                userCartRoutingKeyDeleteUser,
                userId
        );
    }

}
