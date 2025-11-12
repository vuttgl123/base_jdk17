package com.base.ddd.user.application.usecase;

import com.base.ddd.shared.exception.NotFoundException;
import com.base.ddd.user.application.dto.UserDTO;
import com.base.ddd.user.application.mapper.UserApplicationMapper;
import com.base.ddd.user.domain.model.User;
import com.base.ddd.user.domain.model.UserId;
import com.base.ddd.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Restore User Use Case
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RestoreUserUseCase {

    private final UserRepository userRepository;
    private final UserApplicationMapper mapper;

    @Transactional
    public UserDTO execute(Long id) {
        log.info("Restoring user with id: {}", id);

        UserId userId = UserId.of(id);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        user.restore();
        User restoredUser = userRepository.save(user);

        log.info("User restored successfully: {}", id);

        return mapper.toDTO(restoredUser);
    }
}