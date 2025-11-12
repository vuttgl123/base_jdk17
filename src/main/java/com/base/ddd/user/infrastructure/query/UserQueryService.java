package com.base.ddd.user.infrastructure.query;

import com.base.ddd.shared.infrastructure.PageResponse;
import com.base.ddd.shared.infrastructure.SearchRequest;
import com.base.ddd.user.application.dto.UserDTO;
import com.base.ddd.user.application.dto.UserQuery;
import com.base.ddd.user.domain.model.UserStatus;

import java.util.List;

/**
 * User Query Service Interface
 * Optimized for read operations (CQRS pattern)
 */
public interface UserQueryService {

    PageResponse<UserDTO> search(SearchRequest searchRequest);

    PageResponse<UserDTO> filter(UserQuery query);

    List<UserDTO> searchByKeyword(String keyword);

    UserDTO findByEmail(String email);

    UserDTO findByUsername(String username);

    List<UserDTO> findByStatus(UserStatus status);

    long countByStatus(UserStatus status);

    long countTotal();

    long countActive();
}