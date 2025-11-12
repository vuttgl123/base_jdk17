package com.base.ddd.user.application.usecase;

import com.base.ddd.shared.infrastructure.PageResponse;
import com.base.ddd.user.application.dto.UserDTO;
import com.base.ddd.user.application.dto.UserQuery;
import com.base.ddd.user.infrastructure.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Filter User Use Case
 * Advanced filtering with multiple criteria
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FilterUserUseCase {

    private final UserQueryService userQueryService;

    @Transactional(readOnly = true)
    public PageResponse<UserDTO> execute(UserQuery query) {
        log.info("Filtering users with query: {}", query);
        return userQueryService.filter(query);
    }
}