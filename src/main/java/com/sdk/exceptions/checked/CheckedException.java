package com.sdk.exceptions.checked;

import com.sdk.exceptions.core.BaseException;
import com.sdk.exceptions.core.ExceptionContext;

/**
 * Base class for checked exceptions in the SDK.
 * Represents predictable errors that should be handled by calling code.
 */
public class CheckedException extends BaseException {
    
    public static final String CATEGORY = "CHECKED";
    public static final String DEFAULT_SEVERITY = "MEDIUM";
    
    /**
     * Constructs a CheckedException with the specified message.
     *
     * @param message the detail message
     */
    public CheckedException(String message) {
        super(message);
    }
    
    /**
     * Constructs a CheckedException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public CheckedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructs a CheckedException with the specified error code and message.
     *
     * @param errorCode the error code
     * @param message the detail message
     */
    public CheckedException(String errorCode, String message) {
        super(errorCode, message);
    }
    
    /**
     * Constructs a CheckedException with error code, message, and cause.
     *
     * @param errorCode the error code
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public CheckedException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    /**
     * Constructs a CheckedException with all parameters.
     *
     * @param errorCode the error code
     * @param message the detail message
     * @param cause the cause of this exception
     * @param context additional context information
     */
    public CheckedException(String errorCode, String message, Throwable cause, ExceptionContext context) {
        super(errorCode, message, cause, context);
    }
    
    /**
     * Constructs a CheckedException with error code, message and context.
     *
     * @param errorCode the error code
     * @param message the detail message
     * @param context additional context information
     */
    public CheckedException(String errorCode, String message, ExceptionContext context) {
        super(errorCode, message, null, context);
    }
    
    @Override
    public String getCategory() {
        return CATEGORY;
    }
    
    @Override
    public String getSeverity() {
        return DEFAULT_SEVERITY;
    }
}
