package com.base.ddd.user.infrastructure.persistence.repository;

import com.base.ddd.user.domain.model.*;
import com.base.ddd.user.domain.repository.UserRepository;
import com.base.ddd.user.infrastructure.persistence.entity.UserJpaEntity;
import com.base.ddd.user.infrastructure.persistence.mapper.UserPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of Domain Repository Interface
 * Adapts JPA Repository to Domain Repository
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserPersistenceMapper mapper;

    @Override
    public User save(User user) {
        UserJpaEntity entity = mapper.toJpaEntity(user);
        UserJpaEntity saved = jpaRepository.save(entity);
        User savedUser = mapper.toDomainModel(saved);

        // Clear domain events after save
        savedUser.clearDomainEvents();

        return savedUser;
    }

    @Override
    public List<User> saveAll(List<User> users) {
        List<UserJpaEntity> entities = mapper.toJpaEntities(users);
        List<UserJpaEntity> saved = jpaRepository.saveAll(entities);
        List<User> savedUsers = mapper.toDomainModels(saved);

        // Clear domain events after save
        savedUsers.forEach(User::clearDomainEvents);

        return savedUsers;
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomainModel);
    }

    @Override
    public Optional<User> findActiveById(UserId id) {
        return jpaRepository.findActiveById(id.getValue())
                .map(mapper::toDomainModel);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmailAndDeletedFalse(email.getValue())
                .map(mapper::toDomainModel);
    }

    @Override
    public Optional<User> findByUsername(Username username) {
        return jpaRepository.findByUsernameAndDeletedFalse(username.getValue())
                .map(mapper::toDomainModel);
    }

    @Override
    public Optional<User> findActiveByEmail(Email email) {
        return jpaRepository.findActiveByEmail(email.getValue())
                .map(mapper::toDomainModel);
    }

    @Override
    public Optional<User> findActiveByUsername(Username username) {
        return jpaRepository.findActiveByUsername(username.getValue())
                .map(mapper::toDomainModel);
    }

    @Override
    public List<User> findByStatus(UserStatus status) {
        return jpaRepository.findByStatusAndDeletedFalse(status.name())
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAllById(List<UserId> ids) {
        List<Long> idValues = ids.stream()
                .map(UserId::getValue)
                .collect(Collectors.toList());

        return jpaRepository.findAllByIdAndDeletedFalse(idValues)
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmailAndDeletedFalse(email.getValue());
    }

    @Override
    public boolean existsByUsername(Username username) {
        return jpaRepository.existsByUsernameAndDeletedFalse(username.getValue());
    }

    @Override
    public boolean existsByEmailExcludingUser(Email email, UserId userId) {
        return jpaRepository.existsByEmailExcludingUser(
                email.getValue(),
                userId.getValue()
        );
    }

    @Override
    public boolean existsByUsernameExcludingUser(Username username, UserId userId) {
        return jpaRepository.existsByUsernameExcludingUser(
                username.getValue(),
                userId.getValue()
        );
    }

    @Override
    public void delete(UserId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public void delete(User user) {
        if (user != null && user.getId() != null) {
            jpaRepository.deleteById(user.getId().getValue());
        }
    }

    @Override
    public long count() {
        return jpaRepository.countByDeletedFalse();
    }

    @Override
    public long countByStatus(UserStatus status) {
        return jpaRepository.countByStatusAndDeletedFalse(status.name());
    }

    @Override
    public long countActiveUsers() {
        return jpaRepository.countActiveUsers();
    }

    @Override
    public boolean existsById(UserId id) {
        return jpaRepository.existsById(id.getValue());
    }
}