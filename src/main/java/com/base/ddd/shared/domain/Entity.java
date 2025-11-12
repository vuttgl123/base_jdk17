package com.base.ddd.shared.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Base class for all DDD Entities
 * Entity được xác định bởi identity (ID), không phải bởi attributes
 */
@Getter
@Setter
public abstract class Entity<ID extends Serializable> implements Serializable {

    protected ID id;
    protected LocalDateTime createdAt;
    protected String createdBy;
    protected LocalDateTime updatedAt;
    protected String updatedBy;
    protected Long version;

    protected Entity() {
    }

    protected Entity(ID id) {
        this.id = id;
    }

    /**
     * Entities are equal if they have the same ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
