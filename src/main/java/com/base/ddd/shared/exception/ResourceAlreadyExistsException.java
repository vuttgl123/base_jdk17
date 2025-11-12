package com.base.ddd.shared.exception;

/**
 * Resource Already Exists Exception
 */
public class ResourceAlreadyExistsException extends ConflictException {
    public ResourceAlreadyExistsException(String resourceName, String identifier) {
        super(String.format("%s with identifier '%s' already exists", resourceName, identifier));
    }
}