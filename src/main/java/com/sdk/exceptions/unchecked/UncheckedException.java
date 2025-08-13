
package com.sdk.exceptions.unchecked;

import com.sdk.exceptions.core.ExceptionContext;

/**
 * Base class for unchecked exceptions in the SDK.
 * Represents unexpected runtime errors that may not be recoverable.
 */
public class UncheckedException extends RuntimeException {
    
    public static final String CATEGORY = "UNCHECKED";
    public static final String DEFAULT_SEVERITY = "HIGH";
    
    private final String errorId;
    private final String errorCode;
    private final java.time.LocalDateTime timestamp;
    private ExceptionContext context;
    
    /**
     * Constructs an UncheckedException with the specified message.
     *
     * @param message the detail message
     */
    public UncheckedException(String message) {
        super(message);
        this.errorId = java.util.UUID.randomUUID().toString();
        this.errorCode = generateDefaultErrorCode();
        this.timestamp = java.time.LocalDateTime.now();
        this.context = new ExceptionContext();
    }
    
    /**
     * Constructs an UncheckedException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public UncheckedException(String message, Throwable cause) {
        super(message, cause);
        this.errorId = java.util.UUID.randomUUID().toString();
        this.errorCode = generateDefaultErrorCode();
        this.timestamp = java.time.LocalDateTime.now();
        this.context = new ExceptionContext();
    }
    
    /**
     * Constructs an UncheckedException with the specified error code and message.
     *
     * @param errorCode the error code
     * @param message the detail message
     */
    public UncheckedException(String errorCode, String message) {
        super(message);
        this.errorId = java.util.UUID.randomUUID().toString();
        this.errorCode = errorCode;
        this.timestamp = java.time.LocalDateTime.now();
        this.context = new ExceptionContext();
    }
    
    /**
     * Constructs an UncheckedException with error code, message, and cause.
     *
     * @param errorCode the error code
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public UncheckedException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorId = java.util.UUID.randomUUID().toString();
        this.errorCode = errorCode;
        this.timestamp = java.time.LocalDateTime.now();
        this.context = new ExceptionContext();
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
        this.errorId = java.util.UUID.randomUUID().toString();
        this.errorCode = errorCode;
        this.timestamp = java.time.LocalDateTime.now();
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
    public java.time.LocalDateTime getTimestamp() {
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
    public String getCategory() {
        return CATEGORY;
    }
    
    /**
     * Gets the exception severity.
     *
     * @return the severity
     */
    public String getSeverity() {
        return DEFAULT_SEVERITY;
    }
    
    /**
     * Generates a default error code based on the class name.
     *
     * @return the default error code
     */
    private String generateDefaultErrorCode() {
        return getClass().getSimpleName().toUpperCase().replace("EXCEPTION", "_ERROR");
    }
}
