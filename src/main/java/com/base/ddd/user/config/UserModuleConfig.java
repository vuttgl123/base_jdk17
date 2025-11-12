package com.base.ddd.user.config;

import com.base.ddd.shared.domain.DomainEventPublisher;
import com.base.ddd.user.domain.event.UserCreatedEvent;
import com.base.ddd.user.domain.repository.UserRepository;
import com.base.ddd.user.domain.service.UserDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User Module Configuration
 * Wire up domain services and event listeners
 */
@Slf4j
@Configuration
public class UserModuleConfig {

    @Bean
    public UserDomainService userDomainService(UserRepository userRepository) {
        return new UserDomainService(userRepository);
    }

    /**
     * Register domain event subscribers
     */
    @Bean
    public UserEventSubscribers userEventSubscribers(DomainEventPublisher eventPublisher) {
        UserEventSubscribers subscribers = new UserEventSubscribers();

        // Subscribe to UserCreatedEvent
        eventPublisher.subscribe(UserCreatedEvent.class, subscribers::handleUserCreated);

        // Subscribe to other events as needed
        // eventPublisher.subscribe(UserUpdatedEvent.class, subscribers::handleUserUpdated);
        // eventPublisher.subscribe(UserDeletedEvent.class, subscribers::handleUserDeleted);

        log.info("User event subscribers registered");

        return subscribers;
    }

    /**
     * Event subscribers for User domain events
     */
    @Slf4j
    static class UserEventSubscribers {

        public void handleUserCreated(UserCreatedEvent event) {
            log.info("User created event received: userId={}, username={}",
                    event.getUserId(), event.getUsername());

            // Handle event logic here:
            // - Send welcome email
            // - Create user profile
            // - Notify other services
            // - Publish to message queue
        }

        // Add more event handlers as needed
    }
}