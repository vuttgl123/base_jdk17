package com.base.ddd.user.application.usecase;

import com.base.ddd.shared.infrastructure.PageResponse;
import com.base.ddd.shared.infrastructure.SearchRequest;
import com.base.ddd.user.application.dto.UserDTO;
import com.base.ddd.user.infrastructure.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Search User Use Case
 * Delegates to query service for optimized read operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchUserUseCase {

    private final UserQueryService userQueryService;

    @Transactional(readOnly = true)
    public PageResponse<UserDTO> execute(SearchRequest searchRequest) {
        log.info("Searching users with request: {}", searchRequest);
        return userQueryService.search(searchRequest);
    }
}