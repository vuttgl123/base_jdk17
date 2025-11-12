package com.base.ddd.shared.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Publisher for Domain Events
 * Implements the Observer pattern to notify subscribers when events occur
 * This is a simple in-memory implementation
 */
public class DomainEventPublisher {

    private static DomainEventPublisher instance;
    private final List<Consumer<DomainEvent>> subscribers = new ArrayList<>();
    private boolean publishing = false;

    private DomainEventPublisher() {
    }

    /**
     * Get singleton instance
     */
    public static DomainEventPublisher instance() {
        if (instance == null) {
            synchronized (DomainEventPublisher.class) {
                if (instance == null) {
                    instance = new DomainEventPublisher();
                }
            }
        }
        return instance;
    }

    /**
     * Subscribe to all domain events
     * @param subscriber Consumer that will handle the events
     */
    public void subscribe(Consumer<DomainEvent> subscriber) {
        this.subscribers.add(subscriber);
    }

    /**
     * Subscribe to specific type of domain events
     * @param eventType Type of event to subscribe to
     * @param subscriber Consumer that will handle the events
     */
    public <T extends DomainEvent> void subscribe(Class<T> eventType, Consumer<T> subscriber) {
        this.subscribers.add(event -> {
            if (eventType.isInstance(event)) {
                subscriber.accept(eventType.cast(event));
            }
        });
    }

    /**
     * Unsubscribe from domain events
     * @param subscriber The subscriber to remove
     */
    public void unsubscribe(Consumer<DomainEvent> subscriber) {
        this.subscribers.remove(subscriber);
    }

    /**
     * Clear all subscribers
     */
    public void clearSubscribers() {
        this.subscribers.clear();
    }

    /**
     * Publish a single domain event to all subscribers
     * @param event The event to publish
     */
    public void publish(DomainEvent event) {
        if (event == null) {
            return;
        }

        if (publishing) {
            throw new IllegalStateException("Cannot publish event while already publishing");
        }

        try {
            publishing = true;
            for (Consumer<DomainEvent> subscriber : subscribers) {
                try {
                    subscriber.accept(event);
                } catch (Exception e) {
                    // Log error but continue notifying other subscribers
                    System.err.println("Error notifying subscriber: " + e.getMessage());
                }
            }
        } finally {
            publishing = false;
        }
    }

    /**
     * Publish multiple domain events
     * @param events List of events to publish
     */
    public void publishAll(List<DomainEvent> events) {
        if (events == null || events.isEmpty()) {
            return;
        }

        for (DomainEvent event : events) {
            publish(event);
        }
    }

    /**
     * Check if there are any subscribers
     */
    public boolean hasSubscribers() {
        return !subscribers.isEmpty();
    }

    /**
     * Get number of subscribers
     */
    public int subscriberCount() {
        return subscribers.size();
    }
}