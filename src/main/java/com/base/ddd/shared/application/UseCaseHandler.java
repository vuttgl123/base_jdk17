package com.base.ddd.shared.application;

import com.base.ddd.shared.domain.DomainEvent;
import com.base.ddd.shared.domain.DomainEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Use Case Handler - orchestrates use case execution
 * Handles cross-cutting concerns like transaction, event publishing, logging
 */
public class UseCaseHandler {

    private static final Logger log = LoggerFactory.getLogger(UseCaseHandler.class);
    private final DomainEventPublisher eventPublisher;

    public UseCaseHandler() {
        this.eventPublisher = DomainEventPublisher.instance();
    }

    public UseCaseHandler(DomainEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * Execute use case and handle events
     */
    public <I, O> O execute(UseCase<I, O> useCase, I input) throws Exception {
        log.debug("Executing use case: {} with input: {}",
                useCase.getClass().getSimpleName(), input);

        try {
            // Execute the use case
            O output = useCase.execute(input);

            log.debug("Use case executed successfully: {}",
                    useCase.getClass().getSimpleName());

            return output;

        } catch (Exception e) {
            log.error("Error executing use case: {}",
                    useCase.getClass().getSimpleName(), e);
            throw e;
        }
    }

    /**
     * Execute use case with transaction and event publishing
     * This version expects the use case to return events
     */
    public <I, O> O executeWithEvents(
            UseCase<I, O> useCase,
            I input,
            List<DomainEvent> events) throws Exception {

        log.debug("Executing use case with events: {}",
                useCase.getClass().getSimpleName());

        try {
            // Execute the use case
            O output = useCase.execute(input);

            // Publish domain events after successful execution
            if (events != null && !events.isEmpty()) {
                eventPublisher.publishAll(events);
                log.debug("Published {} domain events", events.size());
            }

            return output;

        } catch (Exception e) {
            log.error("Error executing use case with events: {}",
                    useCase.getClass().getSimpleName(), e);
            // Events will not be published on error
            throw e;
        }
    }

    /**
     * Execute use case within a transaction context
     * Template method for subclasses to override transaction behavior
     */
    public <I, O> O executeInTransaction(
            UseCase<I, O> useCase,
            I input) throws Exception {

        log.debug("Executing use case in transaction: {}",
                useCase.getClass().getSimpleName());

        // Subclasses can override to add transaction management
        // For now, just execute normally
        return execute(useCase, input);
    }
}