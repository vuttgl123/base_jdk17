package com.base.ddd.shared.exception;

/**
 * Business Exception
 */
public class BusinessException extends DomainException {

    public BusinessException(String message) {
        super(message);
    }
}