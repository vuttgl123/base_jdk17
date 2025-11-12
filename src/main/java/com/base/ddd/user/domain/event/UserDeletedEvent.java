package com.base.ddd.user.domain.event;


import com.base.ddd.shared.domain.DomainEvent;
import com.base.ddd.user.domain.model.UserId;

/**
 * User Deleted Event
 */
public class UserDeletedEvent extends DomainEvent {

    private final UserId userId;

    public UserDeletedEvent(UserId userId) {
        super();
        this.userId = userId;
    }

    @Override
    public String getEventType() {
        return "user.deleted";
    }

    public UserId getUserId() {
        return userId;
    }
}
