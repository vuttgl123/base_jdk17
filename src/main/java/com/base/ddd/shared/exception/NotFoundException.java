
package com.base.ddd.shared.exception;

/**
 * Not Found Exception
 */
public class NotFoundException extends DomainException {

    public NotFoundException(String message) {
        super(message);
    }
}