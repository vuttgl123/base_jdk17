package com.base.ddd.user.application.usecase;

import com.base.ddd.shared.exception.ValidationException;
import com.base.ddd.user.application.dto.CreateUserCommand;
import com.base.ddd.user.application.dto.UserDTO;
import com.base.ddd.user.application.mapper.UserApplicationMapper;
import com.base.ddd.user.domain.model.Email;
import com.base.ddd.user.domain.model.Password;
import com.base.ddd.user.domain.model.User;
import com.base.ddd.user.domain.model.Username;
import com.base.ddd.user.domain.repository.UserRepository;
import com.base.ddd.user.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Create User Use Case
 * Orchestrates the user creation process
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final UserApplicationMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO execute(CreateUserCommand command) {
        log.info("Creating user with username: {}", command.getUsername());

        try {
            // 1. Create value objects with validation
            Username username = Username.of(command.getUsername());
            Email email = Email.of(command.getEmail());

            // 2. Validate uniqueness through domain service
            userDomainService.validateUniqueConstraints(email, username);

            // 3. Hash password
            Password.validatePlainPassword(command.getPassword());
            String hashedPassword = passwordEncoder.encode(command.getPassword());
            Password password = Password.fromHash(hashedPassword);

            // 4. Create aggregate
            User user = User.create(
                    username,
                    email,
                    password,
                    command.getFullName(),
                    command.getPhoneNumber(),
                    command.getAge(),
                    command.getAddress()
            );

            // 5. Persist
            User savedUser = userRepository.save(user);

            // 6. Publish domain events (if using event publisher)
            // eventPublisher.publish(savedUser.getDomainEvents());

            log.info("User created successfully with id: {}", savedUser.getId());

            // 7. Return DTO
            return mapper.toDTO(savedUser);

        } catch (IllegalArgumentException e) {
            log.error("Validation error creating user: {}", e.getMessage());
            throw new ValidationException(e.getMessage());
        }
    }
}