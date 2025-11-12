
package com.base.ddd.shared.exception;

/**
 * Invalid State Exception
 */
public class InvalidStateException extends BusinessException {
    public InvalidStateException(String message) {
        super(message);
    }
}