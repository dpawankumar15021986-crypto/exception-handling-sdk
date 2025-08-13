package com.sdk.exceptions.utils;

import com.sdk.exceptions.core.ExceptionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;

/**
 * Utility class for logging exceptions with structured information.
 * Provides consistent logging format and context handling.
 */
public class ExceptionLogger {
    
    private static final Logger logger = LoggerFactory.getLogger(ExceptionLogger.class);
    
    private static final String ERROR_ID_KEY = "errorId";
    private static final String ERROR_CODE_KEY = "errorCode";
    private static final String ERROR_CATEGORY_KEY = "errorCategory";
    private static final String ERROR_SEVERITY_KEY = "errorSeverity";
    
    /**
     * Logs an exception with the specified message and level.
     *
     * @param exception the exception to log
     * @param message the log message
     * @param level the log level
     */
    public void logException(Throwable exception, String message, LogLevel level) {
        try {
            setupMDC(exception);
            
            switch (level) {
                case TRACE:
                    logger.trace(message, exception);
                    break;
                case DEBUG:
                    logger.debug(message, exception);
                    break;
                case INFO:
                    logger.info(message, exception);
                    break;
                case WARN:
                    logger.warn(message, exception);
                    break;
                case ERROR:
                    logger.error(message, exception);
                    break;
            }
        } finally {
            clearMDC();
        }
    }
    
    /**
     * Logs an exception with ERROR level.
     *
     * @param exception the exception to log
     * @param message the log message
     */
    public void logException(Throwable exception, String message) {
        logException(exception, message, LogLevel.ERROR);
    }
    
    /**
     * Logs an exception with TRACE level.
     *
     * @param exception the exception to log
     * @param message the log message
     */
    public void logTrace(Throwable exception, String message) {
        logException(exception, message, LogLevel.TRACE);
    }
    
    /**
     * Logs an exception with DEBUG level.
     *
     * @param exception the exception to log
     * @param message the log message
     */
    public void logDebug(Throwable exception, String message) {
        logException(exception, message, LogLevel.DEBUG);
    }
    
    /**
     * Logs an exception with INFO level.
     *
     * @param exception the exception to log
     * @param message the log message
     */
    public void logInfo(Throwable exception, String message) {
        logException(exception, message, LogLevel.INFO);
    }
    
    /**
     * Logs an exception with WARN level.
     *
     * @param exception the exception to log
     * @param message the log message
     */
    public void logWarning(Throwable exception, String message) {
        logException(exception, message, LogLevel.WARN);
    }
    
    /**
     * Logs an exception with ERROR level.
     *
     * @param exception the exception to log
     * @param message the log message
     */
    public void logError(Throwable exception, String message) {
        logException(exception, message, LogLevel.ERROR);
    }
    
    /**
     * Logs a critical exception with ERROR level and additional context.
     *
     * @param exception the exception to log
     * @param message the log message
     */
    public void logCritical(Throwable exception, String message) {
        try {
            setupMDC(exception);
            MDC.put("critical", "true");
            logger.error("CRITICAL: " + message, exception);
        } finally {
            clearMDC();
        }
    }
    
    /**
     * Sets up the MDC (Mapped Diagnostic Context) with exception information.
     *
     * @param exception the exception
     */
    private void setupMDC(Throwable exception) {
        MDC.put("exceptionClass", exception.getClass().getSimpleName());
        MDC.put("exceptionMessage", exception.getMessage());
        
        // Add SDK-specific exception information if available
        if (hasMethod(exception, "getErrorId")) {
            String errorId = invokeMethod(exception, "getErrorId", String.class);
            if (errorId != null) {
                MDC.put(ERROR_ID_KEY, errorId);
            }
        }
        
        if (hasMethod(exception, "getErrorCode")) {
            String errorCode = invokeMethod(exception, "getErrorCode", String.class);
            if (errorCode != null) {
                MDC.put(ERROR_CODE_KEY, errorCode);
            }
        }
        
        if (hasMethod(exception, "getCategory")) {
            String category = invokeMethod(exception, "getCategory", String.class);
            if (category != null) {
                MDC.put(ERROR_CATEGORY_KEY, category);
            }
        }
        
        if (hasMethod(exception, "getSeverity")) {
            String severity = invokeMethod(exception, "getSeverity", String.class);
            if (severity != null) {
                MDC.put(ERROR_SEVERITY_KEY, severity);
            }
        }
        
        // Add context information if available
        if (hasMethod(exception, "getContext")) {
            ExceptionContext context = invokeMethod(exception, "getContext", ExceptionContext.class);
            if (context != null) {
                addContextToMDC(context);
            }
        }
    }
    
    /**
     * Adds exception context to MDC.
     *
     * @param context the exception context
     */
    private void addContextToMDC(ExceptionContext context) {
        Map<String, String> metadata = context.getAllMetadata();
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            MDC.put("ctx_" + entry.getKey(), entry.getValue());
        }
    }
    
    /**
     * Clears the MDC.
     */
    private void clearMDC() {
        MDC.clear();
    }
    
    /**
     * Checks if an object has a specific method.
     *
     * @param obj the object
     * @param methodName the method name
     * @return true if the method exists, false otherwise
     */
    private boolean hasMethod(Object obj, String methodName) {
        try {
            obj.getClass().getMethod(methodName);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
    
    /**
     * Invokes a method on an object and returns the result.
     *
     * @param obj the object
     * @param methodName the method name
     * @param returnType the expected return type
     * @param <T> the return type
     * @return the method result, or null if invocation fails
     */
    @SuppressWarnings("unchecked")
    private <T> T invokeMethod(Object obj, String methodName, Class<T> returnType) {
        try {
            Object result = obj.getClass().getMethod(methodName).invoke(obj);
            if (result != null && returnType.isInstance(result)) {
                return (T) result;
            }
        } catch (Exception e) {
            // Ignore reflection errors
        }
        return null;
    }
    
    /**
     * Log levels for exception logging.
     */
    public enum LogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR
    }
}
