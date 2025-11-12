package com.base.ddd.user.domain.repository;

import com.base.ddd.user.domain.model.*;

import java.util.List;
import java.util.Optional;

/**
 * User Repository Interface (Domain Layer)
 * Defines contract for persistence, implementation in infrastructure layer
 */
public interface UserRepository {

    /**
     * Save user (create or update)
     */
    User save(User user);

    /**
     * Save multiple users
     */
    List<User> saveAll(List<User> users);

    /**
     * Find user by ID
     */
    Optional<User> findById(UserId id);

    /**
     * Find active user by ID
     */
    Optional<User> findActiveById(UserId id);

    /**
     * Find user by email
     */
    Optional<User> findByEmail(Email email);

    /**
     * Find user by username
     */
    Optional<User> findByUsername(Username username);

    /**
     * Find active user by email
     */
    Optional<User> findActiveByEmail(Email email);

    /**
     * Find active user by username
     */
    Optional<User> findActiveByUsername(Username username);

    /**
     * Find users by status
     */
    List<User> findByStatus(UserStatus status);

    /**
     * Find all users by IDs
     */
    List<User> findAllById(List<UserId> ids);

    /**
     * Check if email exists
     */
    boolean existsByEmail(Email email);

    /**
     * Check if username exists
     */
    boolean existsByUsername(Username username);

    /**
     * Check if email exists excluding specific user
     */
    boolean existsByEmailExcludingUser(Email email, UserId userId);

    /**
     * Check if username exists excluding specific user
     */
    boolean existsByUsernameExcludingUser(Username username, UserId userId);

    /**
     * Delete user (hard delete)
     */
    void delete(UserId id);

    /**
     * Delete user entity
     */
    void delete(User user);

    /**
     * Count all users
     */
    long count();

    /**
     * Count users by status
     */
    long countByStatus(UserStatus status);

    /**
     * Count active users
     */
    long countActiveUsers();

    /**
     * Check if user exists by ID
     */
    boolean existsById(UserId id);
}