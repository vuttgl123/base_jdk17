package com.base.ddd.shared.domain;

import java.io.Serializable;

/**
 * Value Object - không có identity, được xác định bởi attributes
 * Immutable và có thể thay thế
 */
public interface ValueObject extends Serializable {

    /**
     * Value objects are equal if all attributes are equal
     */
    @Override
    boolean equals(Object other);

    @Override
    int hashCode();
}