package com.base.ddd.user.presentation.rest;

import com.base.ddd.shared.infrastructure.PageResponse;
import com.base.ddd.shared.infrastructure.SearchRequest;
import com.base.ddd.user.application.dto.UserDTO;
import com.base.ddd.user.application.usecase.*;
import com.base.ddd.user.presentation.mapper.UserPresentationMapper;
import com.base.ddd.user.presentation.rest.request.CreateUserRequest;
import com.base.ddd.user.presentation.rest.request.UpdateUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * User REST Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final RestoreUserUseCase restoreUserUseCase;
    private final SearchUserUseCase searchUserUseCase;
    private final UserPresentationMapper presentationMapper;

    /**
     * Create new user
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("REST: Creating user with username: {}", request.getUsername());

        var command = presentationMapper.toCreateCommand(request);
        UserDTO user = createUserUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Get user by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        log.info("REST: Getting user with id: {}", id);

        UserDTO user = getUserUseCase.execute(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Update user
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        log.info("REST: Updating user with id: {}", id);

        var command = presentationMapper.toUpdateCommand(request);
        UserDTO user = updateUserUseCase.execute(id, command);

        return ResponseEntity.ok(user);
    }

    /**
     * Delete user (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("REST: Deleting user with id: {}", id);

        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restore deleted user
     */
    @PatchMapping("/{id}/restore")
    public ResponseEntity<UserDTO> restoreUser(@PathVariable Long id) {
        log.info("REST: Restoring user with id: {}", id);

        UserDTO user = restoreUserUseCase.execute(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Search users with pagination and filters
     */
    @PostMapping("/search")
    public ResponseEntity<PageResponse<UserDTO>> searchUsers(
            @RequestBody SearchRequest searchRequest) {

        log.info("REST: Searching users");

        PageResponse<UserDTO> result = searchUserUseCase.execute(searchRequest);
        return ResponseEntity.ok(result);
    }

    /**
     * Simple health check
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("User Service is running");
    }
}