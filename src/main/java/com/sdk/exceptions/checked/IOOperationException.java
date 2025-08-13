package com.sdk.exceptions.checked;

import com.sdk.exceptions.core.ExceptionContext;

/**
 * Exception thrown when I/O operations fail.
 * Wraps IOException and provides additional context.
 */
public class IOOperationException extends CheckedException {
    
    public static final String DEFAULT_ERROR_CODE = "IO_OPERATION_ERROR";
    public static final String SEVERITY = "HIGH";
    
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
     * Creates an IOOperationException for file operations.
     *
     * @param operation the operation that failed
     * @param filePath the file path involved
     * @param cause the underlying cause
     */
    public static IOOperationException fileOperation(String operation, String filePath, Throwable cause) {
        return new IOOperationException(DEFAULT_ERROR_CODE, 
              String.format("Failed to %s file: %s", operation, filePath), 
              cause,
              new ExceptionContext()
                  .addContextData("operation", operation)
                  .addContextData("filePath", filePath)
                  .addMetadata("operationType", "file"));
    }
    
    /**
     * Constructs an IOOperationException for network operations.
     *
     * @param operation the operation that failed
     * @param endpoint the network endpoint
     * @param cause the underlying cause
     */
    public IOOperationException(String operation, String endpoint, int timeout, Throwable cause) {
        super(DEFAULT_ERROR_CODE,
              String.format("Failed to %s endpoint: %s (timeout: %dms)", operation, endpoint, timeout),
              cause,
              new ExceptionContext()
                  .addContextData("operation", operation)
                  .addContextData("endpoint", endpoint)
                  .addContextData("timeout", timeout)
                  .addMetadata("operationType", "network"));
    }
    
    @Override
    public String getSeverity() {
        return SEVERITY;
    }
}
