package com.base.ddd.shared.domain;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base class for all domain events
 */
public abstract class DomainEvent {

    private final String eventId;
    private final LocalDateTime occurredOn;

    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = LocalDateTime.now();
    }

    public String getEventId() {
        return eventId;
    }

    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }

    /**
     * Get event type for routing/filtering
     */
    public abstract String getEventType();
}