package com.base.ddd.user.domain.event;

import com.base.ddd.shared.domain.DomainEvent;
import com.base.ddd.user.domain.model.User;
import com.base.ddd.user.domain.model.UserId;

/**
 * User Updated Event
 */
public class UserUpdatedEvent extends DomainEvent {

    private final UserId userId;
    private final String email;
    private final String fullName;

    public UserUpdatedEvent(User user) {
        super();
        this.userId = user.getId();
        this.email = user.getEmail().getValue();
        this.fullName = user.getFullName();
    }

    @Override
    public String getEventType() {
        return "user.updated";
    }

    public UserId getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }
}
