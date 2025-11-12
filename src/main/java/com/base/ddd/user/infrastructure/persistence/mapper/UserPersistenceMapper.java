package com.base.ddd.user.infrastructure.persistence.mapper;

import com.base.ddd.user.domain.model.*;
import com.base.ddd.user.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Mapper between Domain Model and JPA Entity
 */
@Component
public class UserPersistenceMapper {

    /**
     * Convert Domain Model to JPA Entity
     */
    public UserJpaEntity toJpaEntity(User user) {
        if (user == null) {
            return null;
        }

        UserJpaEntity entity = new UserJpaEntity();

        if (user.getId() != null) {
            entity.setId(user.getId().getValue());
        }

        entity.setUsername(user.getUsername().getValue());
        entity.setEmail(user.getEmail().getValue());
        entity.setPasswordHash(user.getPassword().getValue());
        entity.setFullName(user.getFullName());
        entity.setPhoneNumber(user.getPhoneNumber());
        entity.setStatus(user.getStatus().name());
        entity.setAge(user.getAge());
        entity.setAddress(user.getAddress());
        entity.setAvatarUrl(user.getAvatarUrl());
        entity.setDeleted(user.isDeleted());

        // Audit fields
        entity.setCreatedAt(user.getCreatedAt());
        entity.setCreatedBy(user.getCreatedBy());
        entity.setUpdatedAt(user.getUpdatedAt());
        entity.setUpdatedBy(user.getUpdatedBy());
        entity.setVersion(user.getVersion());

        return entity;
    }

    /**
     * Update existing JPA Entity from Domain Model
     */
    public void updateJpaEntity(User user, UserJpaEntity entity) {
        if (user == null || entity == null) {
            return;
        }

        entity.setUsername(user.getUsername().getValue());
        entity.setEmail(user.getEmail().getValue());
        entity.setPasswordHash(user.getPassword().getValue());
        entity.setFullName(user.getFullName());
        entity.setPhoneNumber(user.getPhoneNumber());
        entity.setStatus(user.getStatus().name());
        entity.setAge(user.getAge());
        entity.setAddress(user.getAddress());
        entity.setAvatarUrl(user.getAvatarUrl());
        entity.setDeleted(user.isDeleted());

        // Version will be incremented by JPA
    }

    /**
     * Convert JPA Entity to Domain Model
     */
    public User toDomainModel(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        try {
            // Reconstitute domain model from persistence
            User user = User.reconstitute(
                    UserId.of(entity.getId()),
                    Username.of(entity.getUsername()),
                    Email.of(entity.getEmail()),
                    Password.fromHash(entity.getPasswordHash()),
                    entity.getFullName(),
                    entity.getPhoneNumber(),
                    UserStatus.valueOf(entity.getStatus()),
                    entity.getAge(),
                    entity.getAddress(),
                    entity.getAvatarUrl(),
                    entity.getDeleted() != null ? entity.getDeleted() : false
            );

            // Set audit fields
            if (entity.getCreatedAt() != null) {
                user.setCreatedAt(entity.getCreatedAt());
            }
            if (entity.getCreatedBy() != null) {
                user.setCreatedBy(entity.getCreatedBy());
            }
            if (entity.getUpdatedAt() != null) {
                user.setUpdatedAt(entity.getUpdatedAt());
            }
            if (entity.getUpdatedBy() != null) {
                user.setUpdatedBy(entity.getUpdatedBy());
            }

            return user;

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to map UserJpaEntity to User domain model: " + e.getMessage(), e
            );
        }
    }

    /**
     * Convert list of JPA Entities to Domain Models
     */
    public List<User> toDomainModels(List<UserJpaEntity> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    /**
     * Convert list of Domain Models to JPA Entities
     */
    public List<UserJpaEntity> toJpaEntities(List<User> users) {
        if (users == null) {
            return List.of();
        }

        return users.stream()
                .map(this::toJpaEntity)
                .collect(Collectors.toList());
    }

    /**
     * Convert Optional JPA Entity to Optional Domain Model
     */
    public Optional<User> toDomainModelOptional(Optional<UserJpaEntity> entityOptional) {
        return entityOptional.map(this::toDomainModel);
    }

    /**
     * Convert Page of JPA Entities to Page of Domain Models
     */
    public Page<User> toDomainModelPage(Page<UserJpaEntity> entityPage) {
        if (entityPage == null) {
            return Page.empty();
        }

        List<User> users = entityPage.getContent().stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());

        return new PageImpl<>(users, entityPage.getPageable(), entityPage.getTotalElements());
    }
}