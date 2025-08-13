package com.sdk.exceptions.checked;

import com.sdk.exceptions.core.ExceptionContext;

/**
 * Exception for I/O operation errors.
 * Represents errors that occur during input/output operations.
 */
public class IOOperationException extends CheckedException {

    public static final String DEFAULT_ERROR_CODE = "IO_OPERATION_ERROR";

    /**
     * Constructs an IOOperationException with the specified message.
     *
     * @param message the detail message
     */
    public IOOperationException(String message) {
        super(DEFAULT_ERROR_CODE, message);
    }

    /**
     * Constructs an IOOperationException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public IOOperationException(String message, Throwable cause) {
        super(DEFAULT_ERROR_CODE, message, cause);
    }

    /**
     * Constructs an IOOperationException with the specified error code and message.
     *
     * @param errorCode the error code
     * @param message the detail message
     */
    public IOOperationException(String errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructs an IOOperationException with error code, message, and cause.
     *
     * @param errorCode the error code
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public IOOperationException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Constructs an IOOperationException with all parameters.
     *
     * @param errorCode the error code
     * @param message the detail message
     * @param cause the cause of this exception
     * @param context additional context information
     */
    public IOOperationException(String errorCode, String message, Throwable cause, ExceptionContext context) {
        super(errorCode, message, cause, context);
    }

    /**
     * Constructs an IOOperationException with error code, message and context.
     *
     * @param errorCode the error code
     * @param message the detail message
     * @param context additional context information
     */
    public IOOperationException(String errorCode, String message, ExceptionContext context) {
        super(errorCode, message, context);
    }
}