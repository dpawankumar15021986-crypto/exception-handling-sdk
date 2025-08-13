package com.sdk.exceptions.handlers;

import com.sdk.exceptions.model.ErrorResponse;
import com.sdk.exceptions.model.ExceptionDetails;
import com.sdk.exceptions.utils.ExceptionLogger;
import com.sdk.exceptions.config.ExceptionHandlingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Global exception handler that coordinates multiple exception handlers.
 * Provides centralized exception handling with configurable strategies.
 */
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    private final List<ExceptionHandler<?>> handlers;
    private final ExceptionLogger exceptionLogger;
    private final ExceptionHandlingConfig config;
    private final ConcurrentMap<Class<?>, ExceptionHandler<?>> handlerCache;
    
    /**
     * Constructs a GlobalExceptionHandler with default configuration.
     */
    public GlobalExceptionHandler() {
        this(ExceptionHandlingConfig.defaultConfig());
    }
    
    /**
     * Constructs a GlobalExceptionHandler with the specified configuration.
     *
     * @param config the exception handling configuration
     */
    public GlobalExceptionHandler(ExceptionHandlingConfig config) {
        this.config = config;
        this.exceptionLogger = new ExceptionLogger();
        this.handlers = new ArrayList<>();
        this.handlerCache = new ConcurrentHashMap<>();
        
        initializeDefaultHandlers();
    }
    
    /**
     * Initializes the default exception handlers.
     */
    private void initializeDefaultHandlers() {
        // Add handlers in order of priority
        addHandler(new ErrorHandler(exceptionLogger));
        addHandler(new HttpExceptionHandler(exceptionLogger));
        addHandler(new CheckedExceptionHandler(exceptionLogger));
        addHandler(new UncheckedExceptionHandler(exceptionLogger));
        
        // Sort handlers by priority
        handlers.sort(Comparator.comparingInt(ExceptionHandler::getPriority));
    }
    
    /**
     * Adds a custom exception handler.
     *
     * @param handler the handler to add
     */
    public void addHandler(ExceptionHandler<?> handler) {
        handlers.add(handler);
        handlers.sort(Comparator.comparingInt(ExceptionHandler::getPriority));
        handlerCache.clear(); // Clear cache when handlers change
    }
    
    /**
     * Removes an exception handler.
     *
     * @param handler the handler to remove
     */
    public void removeHandler(ExceptionHandler<?> handler) {
        handlers.remove(handler);
        handlerCache.clear(); // Clear cache when handlers change
    }
    
    /**
     * Handles an exception and returns an error response.
     *
     * @param exception the exception to handle
     * @return the error response
     */
    public ErrorResponse handleException(Throwable exception) {
        try {
            ExceptionHandler<Throwable> handler = findHandler(exception);
            if (handler != null) {
                return handler.handle(exception);
            } else {
                return handleUnknownException(exception);
            }
        } catch (Exception handlingException) {
            logger.error("Error occurred while handling exception", handlingException);
            return handleHandlingError(exception, handlingException);
        }
    }
    
    /**
     * Finds the appropriate handler for the given exception.
     *
     * @param exception the exception
     * @return the handler, or null if not found
     */
    @SuppressWarnings("unchecked")
    private ExceptionHandler<Throwable> findHandler(Throwable exception) {
        Class<?> exceptionClass = exception.getClass();
        
        // Check cache first
        ExceptionHandler<?> cachedHandler = handlerCache.get(exceptionClass);
        if (cachedHandler != null) {
            return (ExceptionHandler<Throwable>) cachedHandler;
        }
        
        // Find handler
        for (ExceptionHandler<?> handler : handlers) {
            if (handler.canHandle(exception)) {
                handlerCache.put(exceptionClass, handler);
                return (ExceptionHandler<Throwable>) handler;
            }
        }
        
        return null;
    }
    
    /**
     * Handles unknown exceptions that don't have specific handlers.
     *
     * @param exception the unknown exception
     * @return the error response
     */
    private ErrorResponse handleUnknownException(Throwable exception) {
        String errorId = UUID.randomUUID().toString();
        
        exceptionLogger.logError(exception, "Unhandled exception occurred: " + exception.getClass().getSimpleName());
        
        ExceptionDetails details = ExceptionDetails.builder()
                .errorId(errorId)
                .errorCode("UNHANDLED_EXCEPTION")
                .message(config.isIncludeExceptionDetails() ? exception.getMessage() : "An unexpected error occurred")
                .category("UNKNOWN")
                .severity("HIGH")
                .timestamp(LocalDateTime.now())
                .stackTrace(config.isIncludeStackTrace() ? getStackTrace(exception) : null)
                .build();
        
        return ErrorResponse.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .details(details)
                .message(config.isIncludeExceptionDetails() ? 
                        "Unhandled exception: " + exception.getMessage() : 
                        "An unexpected error occurred. Please contact support.")
                .build();
    }
    
    /**
     * Handles errors that occur during exception handling.
     *
     * @param originalException the original exception
     * @param handlingException the exception that occurred during handling
     * @return the error response
     */
    private ErrorResponse handleHandlingError(Throwable originalException, Exception handlingException) {
        String errorId = UUID.randomUUID().toString();
        
        logger.error("Critical error: Exception handling failed for {}", 
                originalException.getClass().getSimpleName(), handlingException);
        
        ExceptionDetails details = ExceptionDetails.builder()
                .errorId(errorId)
                .errorCode("EXCEPTION_HANDLING_ERROR")
                .message("Critical error occurred during exception handling")
                .category("SYSTEM")
                .severity("CRITICAL")
                .timestamp(LocalDateTime.now())
                .build();
        
        return ErrorResponse.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .details(details)
                .message("A critical system error has occurred. Please contact support immediately.")
                .build();
    }
    
    /**
     * Gets the stack trace as a string array.
     *
     * @param exception the exception
     * @return the stack trace
     */
    private String[] getStackTrace(Throwable exception) {
        StackTraceElement[] elements = exception.getStackTrace();
        String[] stackTrace = new String[elements.length];
        for (int i = 0; i < elements.length; i++) {
            stackTrace[i] = elements[i].toString();
        }
        return stackTrace;
    }
    
    /**
     * Gets the current configuration.
     *
     * @return the configuration
     */
    public ExceptionHandlingConfig getConfig() {
        return config;
    }
    
    /**
     * Gets the list of registered handlers.
     *
     * @return a copy of the handlers list
     */
    public List<ExceptionHandler<?>> getHandlers() {
        return new ArrayList<>(handlers);
    }
    
    /**
     * Clears the handler cache.
     */
    public void clearCache() {
        handlerCache.clear();
    }
}
