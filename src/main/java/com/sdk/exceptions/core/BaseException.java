package com.sdk.exceptions.core;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Base exception class for all SDK exceptions.
 * Provides common functionality including error codes, timestamps, and context information.
 */
public abstract class BaseException extends Exception {
    
    private final String errorCode;
    private final String errorId;
    private final LocalDateTime timestamp;
    private final ExceptionContext context;
    
    /**
     * Constructs a BaseException with the specified message.
     *
     * @param message the detail message
     */
    public BaseException(String message) {
        this(null, message, null, null);
    }
    
    /**
     * Constructs a BaseException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public BaseException(String message, Throwable cause) {
        this(null, message, cause, null);
    }
    
    /**
     * Constructs a BaseException with the specified error code and message.
     *
     * @param errorCode the error code
     * @param message the detail message
     */
    public BaseException(String errorCode, String message) {
        this(errorCode, message, null, null);
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
        this.errorCode = errorCode != null ? errorCode : generateDefaultErrorCode();
        this.errorId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.context = context != null ? context : new ExceptionContext();
    }
    
    /**
     * Generates a default error code based on the exception class name.
     *
     * @return the default error code
     */
    protected String generateDefaultErrorCode() {
        String className = getClass().getSimpleName();
        return className.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
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
     * Gets the unique error ID.
     *
     * @return the error ID
     */
    public String getErrorId() {
        return errorId;
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
     * @return the exception context
     */
    public ExceptionContext getContext() {
        return context;
    }
    
    /**
     * Gets the category of this exception (e.g., "CHECKED", "UNCHECKED", "ERROR", "HTTP").
     *
     * @return the exception category
     */
    public abstract String getCategory();
    
    /**
     * Gets the severity level of this exception.
     *
     * @return the severity level
     */
    public abstract String getSeverity();
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseException that = (BaseException) o;
        return Objects.equals(errorId, that.errorId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(errorId);
    }
    
    @Override
    public String toString() {
        return String.format("%s{errorCode='%s', errorId='%s', timestamp=%s, message='%s'}",
                getClass().getSimpleName(), errorCode, errorId, timestamp, getMessage());
    }
}
