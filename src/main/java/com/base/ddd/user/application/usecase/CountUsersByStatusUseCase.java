package com.base.ddd.user.application.usecase;

import com.base.ddd.user.domain.model.UserStatus;
import com.base.ddd.user.infrastructure.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Count Users By Status Use Case
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CountUsersByStatusUseCase {

    private final UserQueryService userQueryService;

    @Transactional(readOnly = true)
    public long execute(UserStatus status) {
        log.info("Counting users with status: {}", status);
        return userQueryService.countByStatus(status);
    }
}