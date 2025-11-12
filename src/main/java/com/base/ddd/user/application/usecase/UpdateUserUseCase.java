package com.base.ddd.user.application.usecase;

import com.base.ddd.shared.exception.NotFoundException;
import com.base.ddd.shared.exception.ValidationException;
import com.base.ddd.user.application.dto.UpdateUserCommand;
import com.base.ddd.user.application.dto.UserDTO;
import com.base.ddd.user.application.mapper.UserApplicationMapper;
import com.base.ddd.user.domain.model.Email;
import com.base.ddd.user.domain.model.User;
import com.base.ddd.user.domain.model.UserId;
import com.base.ddd.user.domain.model.UserStatus;
import com.base.ddd.user.domain.repository.UserRepository;
import com.base.ddd.user.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Update User Use Case
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final UserApplicationMapper mapper;

    @Transactional
    public UserDTO execute(Long id, UpdateUserCommand command) {
        log.info("Updating user with id: {}", id);

        try {
            // 1. Find user
            UserId userId = UserId.of(id);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found: " + id));

            // 2. Create value objects with validation
            Email email = command.getEmail() != null
                    ? Email.of(command.getEmail())
                    : user.getEmail();

            // 3. Validate email uniqueness if changed
            if (!email.equals(user.getEmail())) {
                userDomainService.validateEmailUniqueness(userId, email);
            }

            // 4. Update aggregate through business method
            user.updateProfile(
                    email,
                    command.getFullName(),
                    command.getPhoneNumber(),
                    command.getAge(),
                    command.getAddress(),
                    command.getAvatarUrl()
            );

            // 5. Update status if provided
            if (command.getStatus() != null) {
                UserStatus newStatus = UserStatus.valueOf(command.getStatus());
                if (userDomainService.canChangeStatus(user, newStatus)) {
                    user.changeStatus(newStatus);
                } else {
                    throw new ValidationException("Cannot change status from " +
                            user.getStatus() + " to " + newStatus);
                }
            }

            // 6. Persist
            User updatedUser = userRepository.save(user);

            log.info("User updated successfully: {}", id);

            // 7. Return DTO
            return mapper.toDTO(updatedUser);

        } catch (IllegalArgumentException e) {
            log.error("Validation error updating user: {}", e.getMessage());
            throw new ValidationException(e.getMessage());
        }
    }
}