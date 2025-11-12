package com.base.ddd.user.application.usecase;

import com.base.ddd.user.application.dto.UserDTO;
import com.base.ddd.user.application.mapper.UserApplicationMapper;
import com.base.ddd.user.domain.model.Email;
import com.base.ddd.user.domain.model.User;
import com.base.ddd.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Get User By Email Use Case
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserByEmailUseCase {

    private final UserRepository userRepository;
    private final UserApplicationMapper mapper;

    @Transactional(readOnly = true)
    public Optional<UserDTO> execute(String emailStr) {
        log.info("Getting user by email: {}", emailStr);

        Email email = Email.of(emailStr);
        Optional<User> user = userRepository.findByEmail(email);

        return user.map(mapper::toDTO);
    }
}