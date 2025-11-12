package com.base.ddd.user.domain.event;

import com.base.ddd.shared.domain.DomainEvent;
import com.base.ddd.user.domain.model.UserId;

/**
 * Avatar Updated Event
 */
public class AvatarUpdatedEvent extends DomainEvent {

    private final UserId userId;
    private final String avatarUrl;

    public AvatarUpdatedEvent(UserId userId, String avatarUrl) {
        super();
        this.userId = userId;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String getEventType() {
        return "user.avatar.updated";
    }

    public UserId getUserId() {
        return userId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}