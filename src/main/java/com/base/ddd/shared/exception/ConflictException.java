package com.base.ddd.shared.exception;

/**
 * Conflict Exception - 409 (duplicate, already exists)
 */
public class ConflictException extends DomainException {
    public ConflictException(String message) {
        super(message);
    }
}