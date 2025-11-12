package com.base.ddd.user.application.usecase;

import com.base.ddd.shared.exception.NotFoundException;
import com.base.ddd.shared.exception.ValidationException;
import com.base.ddd.user.application.dto.UserDTO;
import com.base.ddd.user.application.mapper.UserApplicationMapper;
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
 * Change User Status Use Case
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChangeUserStatusUseCase {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final UserApplicationMapper mapper;

    @Transactional
    public UserDTO execute(Long id, UserStatus newStatus) {
        log.info("Changing status for user {} to {}", id, newStatus);

        UserId userId = UserId.of(id);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        if (!userDomainService.canChangeStatus(user, newStatus)) {
            throw new ValidationException(
                    "Cannot change status from " + user.getStatus() + " to " + newStatus
            );
        }

        user.changeStatus(newStatus);
        User updatedUser = userRepository.save(user);

        log.info("Status changed successfully for user: {}", id);

        return mapper.toDTO(updatedUser);
    }
}