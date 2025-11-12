package com.base.ddd.user.domain.event;

import com.base.ddd.shared.domain.DomainEvent;
import com.base.ddd.user.domain.model.User;
import com.base.ddd.user.domain.model.UserId;

/**
 * User Created Event
 */
public class UserCreatedEvent extends DomainEvent {

    private final UserId userId;
    private final String username;
    private final String email;

    public UserCreatedEvent(User user) {
        super();
        this.userId = user.getId();
        this.username = user.getUsername().getValue();
        this.email = user.getEmail().getValue();
    }

    @Override
    public String getEventType() {
        return "user.created";
    }

    public UserId getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}