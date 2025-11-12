
package com.base.ddd.shared.exception;

/**
 * Unauthorized Exception - 401
 */
public class UnauthorizedException extends DomainException {
    public UnauthorizedException(String message) {
        super(message);
    }
}