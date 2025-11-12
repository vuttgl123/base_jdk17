package com.base.ddd.user.domain.event;

import com.base.ddd.shared.domain.DomainEvent;
import com.base.ddd.user.domain.model.UserId;
import com.base.ddd.user.domain.model.UserStatus;

/**
 * User Status Changed Event
 */
public class UserStatusChangedEvent extends DomainEvent {

    private final UserId userId;
    private final UserStatus oldStatus;
    private final UserStatus newStatus;

    public UserStatusChangedEvent(UserId userId, UserStatus oldStatus, UserStatus newStatus) {
        super();
        this.userId = userId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }

    @Override
    public String getEventType() {
        return "user.status.changed";
    }

    public UserId getUserId() {
        return userId;
    }

    public UserStatus getOldStatus() {
        return oldStatus;
    }

    public UserStatus getNewStatus() {
        return newStatus;
    }
}