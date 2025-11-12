package com.base.ddd.user.application.usecase;

import com.base.ddd.shared.exception.NotFoundException;
import com.base.ddd.shared.exception.ValidationException;
import com.base.ddd.user.domain.model.Password;
import com.base.ddd.user.domain.model.User;
import com.base.ddd.user.domain.model.UserId;
import com.base.ddd.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Change Password Use Case
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void execute(Long id, String oldPassword, String newPassword) {
        log.info("Changing password for user: {}", id);

        UserId userId = UserId.of(id);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        // Verify old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword().getValue())) {
            throw new ValidationException("Current password is incorrect");
        }

        // Validate and hash new password
        Password.validatePlainPassword(newPassword);
        String hashedPassword = passwordEncoder.encode(newPassword);
        Password newPasswordVO = Password.fromHash(hashedPassword);

        // Change password
        user.changePassword(newPasswordVO);
        userRepository.save(user);

        log.info("Password changed successfully for user: {}", id);
    }
}