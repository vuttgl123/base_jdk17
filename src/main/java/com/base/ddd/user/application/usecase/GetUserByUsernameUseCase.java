package com.base.ddd.user.application.usecase;

import com.base.ddd.user.application.dto.UserDTO;
import com.base.ddd.user.application.mapper.UserApplicationMapper;
import com.base.ddd.user.domain.model.User;
import com.base.ddd.user.domain.model.Username;
import com.base.ddd.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Get User By Username Use Case
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserByUsernameUseCase {

    private final UserRepository userRepository;
    private final UserApplicationMapper mapper;

    @Transactional(readOnly = true)
    public Optional<UserDTO> execute(String usernameStr) {
        log.info("Getting user by username: {}", usernameStr);

        Username username = Username.of(usernameStr);
        Optional<User> user = userRepository.findByUsername(username);

        return user.map(mapper::toDTO);
    }
}