package com.base.ddd.shared.exception;

/**
 * Forbidden Exception - 403
 */
public class ForbiddenException extends DomainException {
    public ForbiddenException(String message) {
        super(message);
    }
}