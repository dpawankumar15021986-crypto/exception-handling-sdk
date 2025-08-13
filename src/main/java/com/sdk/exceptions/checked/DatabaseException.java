
package com.sdk.exceptions.checked;

import com.sdk.exceptions.core.ExceptionContext;

/**
 * Exception for database-related errors.
 * Represents errors that occur during database operations.
 */
public class DatabaseException extends CheckedException {
    
    public static final String DEFAULT_ERROR_CODE = "DATABASE_ERROR";
    
    /**
     * Constructs a DatabaseException with the specified message.
     *
     * @param message the detail message
     */
    public DatabaseException(String message) {
        super(DEFAULT_ERROR_CODE, message);
    }
    
    /**
     * Constructs a DatabaseException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public DatabaseException(String message, Throwable cause) {
        super(DEFAULT_ERROR_CODE, message, cause);
    }
    
    /**
     * Constructs a DatabaseException with the specified error code and message.
     *
     * @param errorCode the error code
     * @param message the detail message
     */
    public DatabaseException(String errorCode, String message) {
        super(errorCode, message);
    }
    
    /**
     * Constructs a DatabaseException with error code, message, and cause.
     *
     * @param errorCode the error code
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public DatabaseException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    /**
     * Constructs a DatabaseException with all parameters.
     *
     * @param errorCode the error code
     * @param message the detail message
     * @param cause the cause of this exception
     * @param context additional context information
     */
    public DatabaseException(String errorCode, String message, Throwable cause, ExceptionContext context) {
        super(errorCode, message, cause, context);
    }
    
    /**
     * Constructs a DatabaseException with error code, message and context.
     *
     * @param errorCode the error code
     * @param message the detail message
     * @param context additional context information
     */
    public DatabaseException(String errorCode, String message, ExceptionContext context) {
        super(errorCode, message, context);
    }
}
