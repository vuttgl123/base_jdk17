package com.base.ddd.user.application.usecase;

import com.base.ddd.shared.exception.BusinessException;
import com.base.ddd.shared.exception.NotFoundException;
import com.base.ddd.user.domain.model.User;
import com.base.ddd.user.domain.model.UserId;
import com.base.ddd.user.domain.repository.UserRepository;
import com.base.ddd.user.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Delete User Use Case (Soft Delete)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteUserUseCase {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;

    @Transactional
    public void execute(Long id) {
        log.info("Deleting user with id: {}", id);

        UserId userId = UserId.of(id);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        // Check business rules
        if (!userDomainService.canDelete(user)) {
            throw new BusinessException("User cannot be deleted");
        }

        // Soft delete
        user.markAsDeleted();
        userRepository.save(user);

        log.info("User deleted successfully: {}", id);
    }
}