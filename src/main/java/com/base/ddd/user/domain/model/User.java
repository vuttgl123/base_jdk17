package com.base.ddd.user.domain.model;

import com.base.ddd.shared.domain.AggregateRoot;
import com.base.ddd.user.domain.event.*;

/**
 * User Aggregate Root
 * Encapsulates all business logic related to User
 */
public class User extends AggregateRoot<UserId> {

    private Username username;
    private Email email;
    private Password password;
    private String fullName;
    private String phoneNumber;
    private UserStatus status;
    private Integer age;
    private String address;
    private String avatarUrl;

    // Private constructor for reconstruction from DB
    private User() {
        super();
    }

    // Factory method for creating new user
    public static User create(
            Username username,
            Email email,
            Password password,
            String fullName,
            String phoneNumber,
            Integer age,
            String address) {

        User user = new User();
        user.username = username;
        user.email = email;
        user.password = password;
        user.fullName = fullName;
        user.phoneNumber = phoneNumber;
        user.age = age;
        user.address = address;
        user.status = UserStatus.ACTIVE;

        user.validate();

        // Register domain event
        user.registerEvent(new UserCreatedEvent(user));

        return user;
    }

    // Factory method for reconstruction from DB
    public static User reconstitute(
            UserId id,
            Username username,
            Email email,
            Password password,
            String fullName,
            String phoneNumber,
            UserStatus status,
            Integer age,
            String address,
            String avatarUrl,
            boolean deleted) {

        User user = new User();
        user.id = id;
        user.username = username;
        user.email = email;
        user.password = password;
        user.fullName = fullName;
        user.phoneNumber = phoneNumber;
        user.status = status;
        user.age = age;
        user.address = address;
        user.avatarUrl = avatarUrl;
        user.setDeleted(deleted);

        return user;
    }

    // Business methods
    public void updateProfile(
            Email email,
            String fullName,
            String phoneNumber,
            Integer age,
            String address,
            String avatarUrl) {

        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.address = address;
        this.avatarUrl = avatarUrl;

        this.validate();

        // Register domain event
        this.registerEvent(new UserUpdatedEvent(this));
    }

    public void changePassword(Password newPassword) {
        if (newPassword == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        this.password = newPassword;
        this.registerEvent(new PasswordChangedEvent(this.id));
    }

    public void changeStatus(UserStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        if (this.status == newStatus) {
            return;
        }

        UserStatus oldStatus = this.status;
        this.status = newStatus;
        this.registerEvent(new UserStatusChangedEvent(this.id, oldStatus, newStatus));
    }

    public void activate() {
        if (this.status == UserStatus.ACTIVE) {
            throw new IllegalStateException("User is already active");
        }
        changeStatus(UserStatus.ACTIVE);
    }

    public void deactivate() {
        if (this.status == UserStatus.INACTIVE) {
            throw new IllegalStateException("User is already inactive");
        }
        changeStatus(UserStatus.INACTIVE);
    }

    public void block() {
        if (this.status == UserStatus.BLOCKED) {
            throw new IllegalStateException("User is already blocked");
        }
        changeStatus(UserStatus.BLOCKED);
    }

    public void updateAvatar(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        this.registerEvent(new AvatarUpdatedEvent(this.id, avatarUrl));
    }

    @Override
    public void markAsDeleted() {
        super.markAsDeleted();
        this.registerEvent(new UserDeletedEvent(this.id));
    }

    @Override
    public void restore() {
        super.restore();
        this.registerEvent(new UserRestoredEvent(this.id, this.username.getValue()));
    }

    @Override
    protected void validate() {
        if (username == null) {
            throw new IllegalStateException("Username is required");
        }
        if (email == null) {
            throw new IllegalStateException("Email is required");
        }
        if (password == null) {
            throw new IllegalStateException("Password is required");
        }
        if (age != null && (age < 1 || age > 150)) {
            throw new IllegalArgumentException("Tuổi phải từ 1-150");
        }
    }

    // Query methods
    public boolean isActive() {
        return status == UserStatus.ACTIVE && !isDeleted();
    }

    public boolean isBlocked() {
        return status == UserStatus.BLOCKED;
    }

    public boolean isPending() {
        return status == UserStatus.PENDING;
    }

    public boolean canLogin() {
        return isActive();
    }

    // Getters
    public Username getUsername() {
        return username;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserStatus getStatus() {
        return status;
    }

    public Integer getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}