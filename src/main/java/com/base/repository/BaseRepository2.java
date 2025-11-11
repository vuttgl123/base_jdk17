package com.base.repository;

import com.base.model.entity.BaseEntity3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository2<T extends BaseEntity3> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> { }