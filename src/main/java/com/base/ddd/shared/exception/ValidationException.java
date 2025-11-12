package com.base.ddd.shared.exception;

/**
 * Validation Exception
 */
public class ValidationException extends DomainException {

    public ValidationException(String message) {
        super(message);
    }
}