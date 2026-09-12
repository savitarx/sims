package com.invisos.sims.common.exception;

/**
 * Thrown when a request is well-formed but the entity is in a state that forbids
 * the operation (for example editing a PUBLISHED exam, or deleting a fee that
 * already has student statuses recorded against it).
 * Mapped to HTTP 409 by {@link GlobalExceptionHandler}.
 */
public class BusinessRuleViolationException extends RuntimeException {

    public BusinessRuleViolationException(String message) {
        super(message);
    }
}
