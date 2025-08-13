
package com.sdk.exceptions.core;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base class for all SDK exceptions.
 * Provides common functionality including error IDs, timestamps, and context management.
 */
public abstract class BaseException extends Exception {
    
    private final String errorId;
    private final String errorCode;
    private final LocalDateTime timestamp;
    private ExceptionContext context;
    
    /**
     * Constructs a BaseException with the specified message.
     *
     * @param message the detail message
     */
    public BaseException(String message) {
        super(message);
        this.errorId = UUID.randomUUID().toString();
        this.errorCode = generateDefaultErrorCode();
        this.timestamp = LocalDateTime.now();
        this.context = new ExceptionContext();
    }
    
    /**
     * Constructs a BaseException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.errorId = UUID.randomUUID().toString();
        this.errorCode = generateDefaultErrorCode();
        this.timestamp = LocalDateTime.now();
        this.context = new ExceptionContext();
    }
    
    /**
     * Constructs a BaseException with the specified error code and message.
     *
     * @param errorCode the error code
     * @param message the detail message
     */
    public BaseException(String errorCode, String message) {
        super(message);
        this.errorId = UUID.randomUUID().toString();
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
        this.context = new ExceptionContext();
    }
    
    /**
     * Constructs a BaseException with error code, message, and cause.
     *
     * @param errorCode the error code
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public BaseException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorId = UUID.randomUUID().toString();
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
        this.context = new ExceptionContext();
    }
    
    /**
     * Constructs a BaseException with all parameters.
     *
     * @param errorCode the error code
     * @param message the detail message
     * @param cause the cause of this exception
     * @param context additional context information
     */
    public BaseException(String errorCode, String message, Throwable cause, ExceptionContext context) {
        super(message, cause);
        this.errorId = UUID.randomUUID().toString();
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
        this.context = context != null ? context : new ExceptionContext();
    }
    
    /**
     * Gets the unique error ID.
     *
     * @return the error ID
     */
    public String getErrorId() {
        return errorId;
    }
    
    /**
     * Gets the error code.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Gets the timestamp when this exception was created.
     *
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    /**
     * Gets the exception context.
     *
     * @return the context
     */
    public ExceptionContext getContext() {
        return context;
    }
    
    /**
     * Sets the exception context.
     *
     * @param context the context to set
     */
    public void setContext(ExceptionContext context) {
        this.context = context;
    }
    
    /**
     * Gets the exception category.
     *
     * @return the category
     */
    public abstract String getCategory();
    
    /**
     * Gets the exception severity.
     *
     * @return the severity
     */
    public abstract String getSeverity();
    
    /**
     * Generates a default error code based on the class name.
     *
     * @return the default error code
     */
    private String generateDefaultErrorCode() {
        return getClass().getSimpleName().toUpperCase().replace("EXCEPTION", "_ERROR");
    }
}
