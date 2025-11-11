package com.base.querydsl;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends GenericRepository<User> {

    Optional<User> findByEmailAndDeletedFalse(String email);

    Optional<User> findByUsernameAndDeletedFalse(String username);

    boolean existsByEmailAndDeletedFalse(String email);

    boolean existsByUsernameAndDeletedFalse(String username);
}
