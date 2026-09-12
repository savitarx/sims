package com.invisos.sims.common.exception;

/**
 * Thrown when a create/update would violate a uniqueness rule that the service
 * checks up-front (for example mapping the same subject to an exam twice).
 * Mapped to HTTP 409 by {@link GlobalExceptionHandler}.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceName, String conflictingValue) {
        super(resourceName + " already exists: " + conflictingValue);
    }
}
