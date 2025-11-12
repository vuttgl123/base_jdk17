package com.base.ddd.shared.config;

import com.base.ddd.shared.domain.DomainEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainEventConfig {

    @Bean
    public DomainEventPublisher domainEventPublisher() {
        // dùng singleton sẵn có
        return DomainEventPublisher.instance();
    }
}