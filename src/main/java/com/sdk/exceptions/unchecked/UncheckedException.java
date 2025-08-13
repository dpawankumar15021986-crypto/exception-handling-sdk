package com.sdk.exceptions.unchecked;

import com.sdk.exceptions.core.ExceptionContext;

/**
 * Base class for unchecked exceptions in the SDK.
 * Represents runtime errors that are typically programming errors.
 */
public class UncheckedException extends RuntimeException {
    
    public static final String CATEGORY = "UNCHECKED";
    public static final String DEFAULT_SEVERITY = "MEDIUM";
    
    private final String errorCode;
    private final String errorId;
    private final java.time.LocalDateTime timestamp;
    private final ExceptionContext context;
    
    /**
     * Constructs an UncheckedException with the specified message.
     *
     * @param message the detail message
     */
    public UncheckedException(String message) {
        this(null, message, null, null);
    }
    
    /**
     * Constructs an UncheckedException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public UncheckedException(String message, Throwable cause) {
        this(null, message, cause, null);
    }
    
    /**
     * Constructs an UncheckedException with the specified error code and message.
     *
     * @param errorCode the error code
     * @param message the detail message
     */
    public UncheckedException(String errorCode, String message) {
        this(errorCode, message, null, null);
    }
    
    /**
     * Constructs an UncheckedException with all parameters.
     *
     * @param errorCode the error code
     * @param message the detail message
     * @param cause the cause of this exception
     * @param context additional context information
     */
    public UncheckedException(String errorCode, String message, Throwable cause, ExceptionContext context) {
        super(message, cause);
        this.errorCode = errorCode != null ? errorCode : generateDefaultErrorCode();
        this.errorId = java.util.UUID.randomUUID().toString();
        this.timestamp = java.time.LocalDateTime.now();
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
    public java.time.LocalDateTime getTimestamp() {
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
     * Gets the category of this exception.
     *
     * @return the exception category
     */
    public String getCategory() {
        return CATEGORY;
    }
    
    /**
     * Gets the severity level of this exception.
     *
     * @return the severity level
     */
    public String getSeverity() {
        return DEFAULT_SEVERITY;
    }
    
    @Override
    public String toString() {
        return String.format("%s{errorCode='%s', errorId='%s', timestamp=%s, message='%s'}",
                getClass().getSimpleName(), errorCode, errorId, timestamp, getMessage());
    }
}
