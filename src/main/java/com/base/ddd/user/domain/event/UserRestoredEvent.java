package com.base.ddd.user.domain.event;

import com.base.ddd.shared.domain.DomainEvent;
import com.base.ddd.user.domain.model.UserId;

/**
 * User Restored Event
 */
public class UserRestoredEvent extends DomainEvent {

    private final UserId userId;
    private final String username;

    public UserRestoredEvent(UserId userId, String username) {
        super();
        this.userId = userId;
        this.username = username;
    }

    @Override
    public String getEventType() {
        return "user.restored";
    }

    public UserId getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}