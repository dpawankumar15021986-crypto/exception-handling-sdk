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
package com.sdk.exceptions.checked;

import com.sdk.exceptions.core.ExceptionContext;

/**
 * Exception for I/O operation errors.
 */
public class IOOperationException extends CheckedException {
    
    public static final String DEFAULT_ERROR_CODE = "IO_ERROR";
    
    /**
     * Constructs an IOOperationException with the specified message.
     *
     * @param message the detail message
     */
    public IOOperationException(String message) {
        super(message);
    }
    
    /**
     * Constructs an IOOperationException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public IOOperationException(String message, Throwable cause) {
        super(message, cause);
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
     * Factory method for file not found errors.
     */
    public static IOOperationException fileNotFound(String filePath, Throwable cause) {
        ExceptionContext context = ExceptionContext.builder()
                .addContextData("filePath", filePath)
                .addMetadata("errorType", "file_not_found")
                .build();
        
        return new IOOperationException(DEFAULT_ERROR_CODE,
                String.format("File not found: %s", filePath),
                cause, context);
    }
    
    /**
     * Factory method for permission denied errors.
     */
    public static IOOperationException permissionDenied(String filePath, String operation, Throwable cause) {
        ExceptionContext context = ExceptionContext.builder()
                .addContextData("filePath", filePath)
                .addContextData("operation", operation)
                .addMetadata("errorType", "permission_denied")
                .build();
        
        return new IOOperationException(DEFAULT_ERROR_CODE,
                String.format("Permission denied for %s operation on: %s", operation, filePath),
                cause, context);
    }
    
    /**
     * Factory method for disk space errors.
     */
    public static IOOperationException diskSpaceExhausted(String path, long requiredBytes, Throwable cause) {
        ExceptionContext context = ExceptionContext.builder()
                .addContextData("path", path)
                .addContextData("requiredBytes", requiredBytes)
                .addMetadata("errorType", "disk_space")
                .build();
        
        return new IOOperationException(DEFAULT_ERROR_CODE,
                String.format("Insufficient disk space on %s, required: %d bytes", path, requiredBytes),
                cause, context);
    }
}
