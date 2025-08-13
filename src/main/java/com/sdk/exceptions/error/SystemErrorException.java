
package com.sdk.exceptions.error;

import com.sdk.exceptions.core.BaseException;
import com.sdk.exceptions.core.ExceptionContext;

/**
 * Exception wrapper for system-level errors.
 * Wraps Error instances to provide consistent handling and context.
 */
public class SystemErrorException extends BaseException {
    
    public static final String CATEGORY = "ERROR";
    public static final String DEFAULT_SEVERITY = "CRITICAL";
    public static final String DEFAULT_ERROR_CODE = "SYSTEM_ERROR";
    
    /**
     * Constructs a SystemErrorException with an Error.
     *
     * @param error the system error
     */
    public SystemErrorException(Error error) {
        super(DEFAULT_ERROR_CODE, error != null ? error.getMessage() : "System error occurred", error);
    }
    
    /**
     * Constructs a SystemErrorException with an Error and context.
     *
     * @param error the system error
     * @param context additional context information
     */
    public SystemErrorException(Error error, ExceptionContext context) {
        super(DEFAULT_ERROR_CODE, error != null ? error.getMessage() : "System error occurred", error, context);
    }
    
    /**
     * Constructs a SystemErrorException for OutOfMemoryError with memory info.
     *
     * @param error the OutOfMemoryError
     * @param memoryInfo memory information
     */
    public SystemErrorException(OutOfMemoryError error, MemoryInfo memoryInfo) {
        super(DEFAULT_ERROR_CODE, error != null ? error.getMessage() : "Out of memory error", error, 
              createMemoryContext(memoryInfo));
    }
    
    /**
     * Constructs a SystemErrorException for StackOverflowError with stack depth.
     *
     * @param error the StackOverflowError
     * @param stackDepth the stack depth when error occurred
     */
    public SystemErrorException(StackOverflowError error, int stackDepth) {
        super(DEFAULT_ERROR_CODE, error != null ? error.getMessage() : "Stack overflow error", error,
              createStackContext(stackDepth));
    }
    
    /**
     * Factory method for out of memory errors.
     *
     * @param component the component that failed
     * @param memoryRequested the amount of memory requested
     * @param cause the original OutOfMemoryError
     * @return SystemErrorException instance
     */
    public static SystemErrorException outOfMemory(String component, long memoryRequested, OutOfMemoryError cause) {
        ExceptionContext context = ExceptionContext.builder()
                .addContextData("component", component)
                .addContextData("memoryRequested", memoryRequested)
                .addMetadata("errorType", "memory")
                .build();
        
        return new SystemErrorException(cause, context);
    }
    
    /**
     * Factory method for stack overflow errors.
     *
     * @param methodName the method where overflow occurred
     * @param stackDepth the stack depth
     * @param cause the original StackOverflowError
     * @return SystemErrorException instance
     */
    public static SystemErrorException stackOverflow(String methodName, int stackDepth, StackOverflowError cause) {
        ExceptionContext context = ExceptionContext.builder()
                .addContextData("methodName", methodName)
                .addContextData("stackDepth", stackDepth)
                .addMetadata("errorType", "stack")
                .build();
        
        return new SystemErrorException(cause, context);
    }
    
    /**
     * Factory method for thread death errors.
     *
     * @param threadName the name of the thread
     * @param cause the original ThreadDeath
     * @return SystemErrorException instance
     */
    public static SystemErrorException threadDeath(String threadName, ThreadDeath cause) {
        ExceptionContext context = ExceptionContext.builder()
                .addContextData("threadName", threadName)
                .addMetadata("errorType", "thread")
                .build();
        
        return new SystemErrorException(cause, context);
    }
    
    /**
     * Factory method for class loading errors.
     *
     * @param className the name of the class that failed to load
     * @param cause the original NoClassDefFoundError
     * @return SystemErrorException instance
     */
    public static SystemErrorException classLoadingError(String className, NoClassDefFoundError cause) {
        ExceptionContext context = ExceptionContext.builder()
                .addContextData("className", className)
                .addMetadata("errorType", "classloading")
                .build();
        
        return new SystemErrorException(cause, context);
    }
    
    @Override
    public String getCategory() {
        return CATEGORY;
    }
    
    @Override
    public String getSeverity() {
        return DEFAULT_SEVERITY;
    }
    
    private static ExceptionContext createMemoryContext(MemoryInfo memoryInfo) {
        ExceptionContext.Builder builder = ExceptionContext.builder()
                .addMetadata("errorType", "memory");
        
        if (memoryInfo != null) {
            builder.addContextData("totalMemory", memoryInfo.getTotalMemory())
                   .addContextData("freeMemory", memoryInfo.getFreeMemory())
                   .addContextData("maxMemory", memoryInfo.getMaxMemory());
        }
        
        return builder.build();
    }
    
    private static ExceptionContext createStackContext(int stackDepth) {
        return ExceptionContext.builder()
                .addContextData("stackDepth", stackDepth)
                .addMetadata("errorType", "stack")
                .build();
    }
    
    /**
     * Memory information class for OutOfMemoryError context.
     */
    public static class MemoryInfo {
        private final long totalMemory;
        private final long freeMemory;
        private final long maxMemory;
        
        public MemoryInfo(long totalMemory, long freeMemory, long maxMemory) {
            this.totalMemory = totalMemory;
            this.freeMemory = freeMemory;
            this.maxMemory = maxMemory;
        }
        
        public long getTotalMemory() { return totalMemory; }
        public long getFreeMemory() { return freeMemory; }
        public long getMaxMemory() { return maxMemory; }
    }
}
