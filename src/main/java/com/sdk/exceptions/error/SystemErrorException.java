package com.sdk.exceptions.error;

import com.sdk.exceptions.core.ExceptionContext;

/**
 * Exception wrapper for system-level errors.
 * Wraps Error instances to provide consistent handling and context.
 */
public class SystemErrorException extends RuntimeException {
    
    public static final String CATEGORY = "ERROR";
    public static final String SEVERITY = "CRITICAL";
    
    private final String errorCode;
    private final String errorId;
    private final java.time.LocalDateTime timestamp;
    private final ExceptionContext context;
    private final String errorType;
    
    /**
     * Constructs a SystemErrorException wrapping an Error.
     *
     * @param error the system error to wrap
     */
    public SystemErrorException(Error error) {
        this(error, null);
    }
    
    /**
     * Constructs a SystemErrorException wrapping an Error with additional context.
     *
     * @param error the system error to wrap
     * @param context additional context information
     */
    public SystemErrorException(Error error, ExceptionContext context) {
        super(String.format("System error occurred: %s", error.getMessage()), error);
        this.errorCode = generateErrorCode(error);
        this.errorId = java.util.UUID.randomUUID().toString();
        this.timestamp = java.time.LocalDateTime.now();
        this.context = context != null ? context : createDefaultContext(error);
        this.errorType = error.getClass().getSimpleName();
    }
    
    /**
     * Constructs a SystemErrorException for OutOfMemoryError.
     *
     * @param error the OutOfMemoryError
     * @param memoryInfo additional memory information
     */
    public SystemErrorException(OutOfMemoryError error, MemoryInfo memoryInfo) {
        super(String.format("Out of memory error: %s", error.getMessage()), error);
        this.errorCode = "OUT_OF_MEMORY_ERROR";
        this.errorId = java.util.UUID.randomUUID().toString();
        this.timestamp = java.time.LocalDateTime.now();
        this.errorType = "OutOfMemoryError";
        
        this.context = new ExceptionContext()
                .addContextData("errorType", errorType)
                .addContextData("maxMemory", memoryInfo.getMaxMemory())
                .addContextData("totalMemory", memoryInfo.getTotalMemory())
                .addContextData("freeMemory", memoryInfo.getFreeMemory())
                .addContextData("usedMemory", memoryInfo.getUsedMemory())
                .addMetadata("severity", SEVERITY)
                .addMetadata("category", CATEGORY);
    }
    
    /**
     * Constructs a SystemErrorException for StackOverflowError.
     *
     * @param error the StackOverflowError
     * @param stackDepth the approximate stack depth when the error occurred
     */
    public SystemErrorException(StackOverflowError error, int stackDepth) {
        super(String.format("Stack overflow error at depth ~%d: %s", stackDepth, error.getMessage()), error);
        this.errorCode = "STACK_OVERFLOW_ERROR";
        this.errorId = java.util.UUID.randomUUID().toString();
        this.timestamp = java.time.LocalDateTime.now();
        this.errorType = "StackOverflowError";
        
        this.context = new ExceptionContext()
                .addContextData("errorType", errorType)
                .addContextData("stackDepth", stackDepth)
                .addMetadata("severity", SEVERITY)
                .addMetadata("category", CATEGORY);
    }
    
    /**
     * Generates an error code based on the Error type.
     *
     * @param error the error instance
     * @return the generated error code
     */
    private String generateErrorCode(Error error) {
        String className = error.getClass().getSimpleName();
        return className.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
    }
    
    /**
     * Creates default context for an Error.
     *
     * @param error the error instance
     * @return the default context
     */
    private ExceptionContext createDefaultContext(Error error) {
        return new ExceptionContext()
                .addContextData("errorType", error.getClass().getSimpleName())
                .addContextData("errorMessage", error.getMessage())
                .addMetadata("severity", SEVERITY)
                .addMetadata("category", CATEGORY);
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
     * Gets the original error type.
     *
     * @return the error type
     */
    public String getErrorType() {
        return errorType;
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
        return SEVERITY;
    }
    
    /**
     * Helper class for memory information.
     */
    public static class MemoryInfo {
        private final long maxMemory;
        private final long totalMemory;
        private final long freeMemory;
        
        public MemoryInfo() {
            Runtime runtime = Runtime.getRuntime();
            this.maxMemory = runtime.maxMemory();
            this.totalMemory = runtime.totalMemory();
            this.freeMemory = runtime.freeMemory();
        }
        
        public long getMaxMemory() {
            return maxMemory;
        }
        
        public long getTotalMemory() {
            return totalMemory;
        }
        
        public long getFreeMemory() {
            return freeMemory;
        }
        
        public long getUsedMemory() {
            return totalMemory - freeMemory;
        }
    }
}
