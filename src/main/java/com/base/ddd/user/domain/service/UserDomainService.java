package com.base.ddd.user.domain.service;

import com.base.ddd.shared.exception.ValidationException;
import com.base.ddd.user.domain.model.*;
import com.base.ddd.user.domain.repository.UserRepository;

import java.util.List;

/**
 * User Domain Service
 * Handles complex business logic that doesn't naturally fit in aggregate
 */
public class UserDomainService {

    private final UserRepository userRepository;

    public UserDomainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Validate uniqueness before creating user
     */
    public void validateUniqueConstraints(Email email, Username username) {
        if (userRepository.existsByEmail(email)) {
            throw new ValidationException("Email đã tồn tại: " + email.getValue());
        }

        if (userRepository.existsByUsername(username)) {
            throw new ValidationException("Username đã tồn tại: " + username.getValue());
        }
    }

    /**
     * Validate email uniqueness for update
     */
    public void validateEmailUniqueness(UserId userId, Email email) {
        if (userRepository.existsByEmailExcludingUser(email, userId)) {
            throw new ValidationException("Email đã được sử dụng bởi user khác");
        }
    }

    /**
     * Validate username uniqueness for update
     */
    public void validateUsernameUniqueness(UserId userId, Username username) {
        if (userRepository.existsByUsernameExcludingUser(username, userId)) {
            throw new ValidationException("Username đã được sử dụng bởi user khác");
        }
    }

    /**
     * Check if user can be deleted
     */
    public boolean canDelete(User user) {
        if (user == null) {
            return false;
        }

        // Already deleted
        if (user.isDeleted()) {
            return false;
        }

        // Add business rules:
        // - Admin users cannot be deleted
        // - Users with active orders cannot be deleted
        // - etc.

        return true;
    }

    /**
     * Check if user can be restored
     */
    public boolean canRestore(User user) {
        if (user == null) {
            return false;
        }

        return user.isDeleted();
    }

    /**
     * Check if user can change status
     */
    public boolean canChangeStatus(User user, UserStatus newStatus) {
        if (user == null || newStatus == null) {
            return false;
        }

        // Already has this status
        if (user.getStatus() == newStatus) {
            return false;
        }

        // Deleted users cannot change status
        if (user.isDeleted()) {
            return false;
        }

        // Business rules for status transitions
        UserStatus currentStatus = user.getStatus();

        // Blocked users must go through INACTIVE before ACTIVE
        if (currentStatus == UserStatus.BLOCKED && newStatus == UserStatus.ACTIVE) {
            return false;
        }

        // Pending users can only go to ACTIVE or BLOCKED
        if (currentStatus == UserStatus.PENDING &&
                newStatus != UserStatus.ACTIVE &&
                newStatus != UserStatus.BLOCKED) {
            return false;
        }

        return true;
    }

    /**
     * Check if user can login
     */
    public boolean canLogin(User user) {
        if (user == null) {
            return false;
        }

        return user.canLogin();
    }

    /**
     * Transfer ownership between users (example of multi-aggregate operation)
     */
    public void transferOwnership(User fromUser, User toUser) {
        if (!fromUser.isActive()) {
            throw new ValidationException("Source user must be active");
        }

        if (!toUser.isActive()) {
            throw new ValidationException("Target user must be active");
        }

        // Implement ownership transfer logic
        // This is where you'd coordinate between multiple aggregates
    }

    /**
     * Bulk activate users
     */
    public void bulkActivateUsers(List<UserId> userIds) {
        List<User> users = userRepository.findAllById(userIds);

        for (User user : users) {
            if (canChangeStatus(user, UserStatus.ACTIVE)) {
                user.activate();
            }
        }

        userRepository.saveAll(users);
    }

    /**
     * Bulk deactivate users
     */
    public void bulkDeactivateUsers(List<UserId> userIds) {
        List<User> users = userRepository.findAllById(userIds);

        for (User user : users) {
            if (canChangeStatus(user, UserStatus.INACTIVE)) {
                user.deactivate();
            }
        }

        userRepository.saveAll(users);
    }

    /**
     * Check if username is available
     */
    public boolean isUsernameAvailable(Username username) {
        return !userRepository.existsByUsername(username);
    }

    /**
     * Check if email is available
     */
    public boolean isEmailAvailable(Email email) {
        return !userRepository.existsByEmail(email);
    }

    /**
     * Get user statistics
     */
    public UserStatistics getUserStatistics() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countActiveUsers();
        long blockedUsers = userRepository.countByStatus(UserStatus.BLOCKED);
        long pendingUsers = userRepository.countByStatus(UserStatus.PENDING);

        return new UserStatistics(totalUsers, activeUsers, blockedUsers, pendingUsers);
    }

    /**
     * Inner class for user statistics
     */
    public static class UserStatistics {
        private final long totalUsers;
        private final long activeUsers;
        private final long blockedUsers;
        private final long pendingUsers;

        public UserStatistics(long totalUsers, long activeUsers, long blockedUsers, long pendingUsers) {
            this.totalUsers = totalUsers;
            this.activeUsers = activeUsers;
            this.blockedUsers = blockedUsers;
            this.pendingUsers = pendingUsers;
        }

        public long getTotalUsers() {
            return totalUsers;
        }

        public long getActiveUsers() {
            return activeUsers;
        }

        public long getBlockedUsers() {
            return blockedUsers;
        }

        public long getPendingUsers() {
            return pendingUsers;
        }

        public long getInactiveUsers() {
            return totalUsers - activeUsers - blockedUsers - pendingUsers;
        }
    }
}