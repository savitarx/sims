package com.invisos.sims.common.exception;

/**
 * Thrown when request values are individually valid but wrong in combination —
 * cases Bean Validation cannot express, such as marks exceeding an exam
 * subject's maximum, or an end date preceding a start date.
 * Mapped to HTTP 400 by {@link GlobalExceptionHandler}.
 */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}
