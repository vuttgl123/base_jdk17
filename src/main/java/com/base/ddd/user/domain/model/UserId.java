package com.base.ddd.user.domain.model;

import com.base.ddd.shared.domain.ValueObject;

import java.util.Objects;

/**
 * UserId Value Object
 */
public class UserId implements ValueObject {

    private final Long value;

    private UserId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("UserId must be positive");
        }
        this.value = value;
    }

    public static UserId of(Long value) {
        return new UserId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(value, userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}