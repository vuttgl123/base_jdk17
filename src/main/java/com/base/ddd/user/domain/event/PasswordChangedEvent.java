package com.base.ddd.user.domain.event;

import com.base.ddd.shared.domain.DomainEvent;
import com.base.ddd.user.domain.model.UserId;

/**
 * Password Changed Event
 */
public class PasswordChangedEvent extends DomainEvent {

    private final UserId userId;

    public PasswordChangedEvent(UserId userId) {
        super();
        this.userId = userId;
    }

    @Override
    public String getEventType() {
        return "user.password.changed";
    }

    public UserId getUserId() {
        return userId;
    }
}