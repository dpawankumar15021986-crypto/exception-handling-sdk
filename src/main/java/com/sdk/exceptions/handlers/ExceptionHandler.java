package com.sdk.exceptions.handlers;

import com.sdk.exceptions.model.ErrorResponse;

/**
 * Interface for exception handlers.
 * Defines the contract for handling different types of exceptions.
 *
 * @param <T> the type of exception this handler can process
 */
public interface ExceptionHandler<T extends Throwable> {
    
    /**
     * Checks if this handler can handle the given exception.
     *
     * @param exception the exception to check
     * @return true if this handler can handle the exception, false otherwise
     */
    boolean canHandle(Throwable exception);
    
    /**
     * Handles the exception and returns an error response.
     *
     * @param exception the exception to handle
     * @return the error response
     */
    ErrorResponse handle(T exception);
    
    /**
     * Gets the priority of this handler.
     * Lower values indicate higher priority.
     *
     * @return the priority value
     */
    default int getPriority() {
        return 100;
    }
    
    /**
     * Gets the exception type this handler supports.
     *
     * @return the exception class
     */
    Class<T> getExceptionType();
}
