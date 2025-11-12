package com.base.ddd.user.application.usecase;

import com.base.ddd.shared.exception.NotFoundException;
import com.base.ddd.user.domain.model.User;
import com.base.ddd.user.domain.model.UserId;
import com.base.ddd.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Update Avatar Use Case
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateAvatarUseCase {

    private final UserRepository userRepository;

    @Transactional
    public void execute(Long id, String avatarUrl) {
        log.info("Updating avatar for user: {}", id);

        UserId userId = UserId.of(id);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        user.updateAvatar(avatarUrl);
        userRepository.save(user);

        log.info("Avatar updated successfully for user: {}", id);
    }
}