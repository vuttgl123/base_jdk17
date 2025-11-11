package com.base.querydsl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository<E extends AbstractBaseEntity> extends JpaRepository<E, Long>, QuerydslPredicateExecutor<E> {
}
