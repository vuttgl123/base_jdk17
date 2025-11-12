package com.base.ddd.shared.exception;

/**
 * Concurrent Modification Exception (Optimistic Locking)
 */
public class ConcurrentModificationException extends DomainException {
    public ConcurrentModificationException(String message) {
        super(message);
    }

    public ConcurrentModificationException() {
        super("The resource was modified by another user. Please refresh and try again.");
    }
}