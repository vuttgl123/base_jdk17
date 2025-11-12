package com.base.ddd.user.infrastructure.persistence.repository;

import com.base.ddd.user.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository
 */
@Repository
public interface UserJpaRepository extends
        JpaRepository<UserJpaEntity, Long>,
        QuerydslPredicateExecutor<UserJpaEntity> {

    // Find by email
    Optional<UserJpaEntity> findByEmailAndDeletedFalse(String email);

    Optional<UserJpaEntity> findByEmail(String email);

    // Find by username
    Optional<UserJpaEntity> findByUsernameAndDeletedFalse(String username);

    Optional<UserJpaEntity> findByUsername(String username);

    // Find by status
    List<UserJpaEntity> findByStatusAndDeletedFalse(String status);

    List<UserJpaEntity> findByStatus(String status);

    // Exists checks
    boolean existsByEmailAndDeletedFalse(String email);

    boolean existsByUsernameAndDeletedFalse(String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM UserJpaEntity u " +
            "WHERE u.email = :email AND u.id <> :userId AND u.deleted = false")
    boolean existsByEmailExcludingUser(@Param("email") String email, @Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM UserJpaEntity u " +
            "WHERE u.username = :username AND u.id <> :userId AND u.deleted = false")
    boolean existsByUsernameExcludingUser(@Param("username") String username, @Param("userId") Long userId);

    // Count queries
    long countByDeletedFalse();

    long countByStatusAndDeletedFalse(String status);

    @Query("SELECT COUNT(u) FROM UserJpaEntity u WHERE u.status = 'ACTIVE' AND u.deleted = false")
    long countActiveUsers();

    // Find active user
    @Query("SELECT u FROM UserJpaEntity u " +
            "WHERE u.id = :id AND u.status = 'ACTIVE' AND u.deleted = false")
    Optional<UserJpaEntity> findActiveById(@Param("id") Long id);

    @Query("SELECT u FROM UserJpaEntity u " +
            "WHERE u.email = :email AND u.status = 'ACTIVE' AND u.deleted = false")
    Optional<UserJpaEntity> findActiveByEmail(@Param("email") String email);

    @Query("SELECT u FROM UserJpaEntity u " +
            "WHERE u.username = :username AND u.status = 'ACTIVE' AND u.deleted = false")
    Optional<UserJpaEntity> findActiveByUsername(@Param("username") String username);

    // Find all including deleted
    @Query("SELECT u FROM UserJpaEntity u WHERE u.id = :id")
    Optional<UserJpaEntity> findByIdIncludingDeleted(@Param("id") Long id);

    // Bulk operations
    @Query("SELECT u FROM UserJpaEntity u WHERE u.id IN :ids AND u.deleted = false")
    List<UserJpaEntity> findAllByIdAndDeletedFalse(@Param("ids") List<Long> ids);
}