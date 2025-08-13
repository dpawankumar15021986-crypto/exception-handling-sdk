package com.sdk.exceptions.handlers;

import com.sdk.exceptions.error.SystemErrorException;
import com.sdk.exceptions.model.ErrorResponse;
import com.sdk.exceptions.model.ExceptionDetails;
import com.sdk.exceptions.utils.ExceptionLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Handler for system errors.
 * Provides standardized handling for critical system-level errors.
 */
public class ErrorHandler implements ExceptionHandler<SystemErrorException> {
    
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);
    private final ExceptionLogger exceptionLogger;
    
    public ErrorHandler() {
        this.exceptionLogger = new ExceptionLogger();
    }
    
    public ErrorHandler(ExceptionLogger exceptionLogger) {
        this.exceptionLogger = exceptionLogger;
    }
    
    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof SystemErrorException;
    }
    
    @Override
    public ErrorResponse handle(SystemErrorException exception) {
        // Log the error with critical severity
        exceptionLogger.logCritical(exception, "Critical system error occurred");
        
        // Create exception details
        ExceptionDetails details = ExceptionDetails.builder()
                .errorId(exception.getErrorId())
                .errorCode(exception.getErrorCode())
                .message(getSanitizedMessage(exception))
                .category(exception.getCategory())
                .severity(exception.getSeverity())
                .timestamp(exception.getTimestamp())
                .context(exception.getContext())
                .stackTrace(getStackTrace(exception))
                .build();
        
        // Create error response
        return ErrorResponse.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .details(details)
                .message("A critical system error has occurred. Please contact support.")
                .build();
    }
    
    @Override
    public int getPriority() {
        return 10; // Highest priority for system errors
    }
    
    @Override
    public Class<SystemErrorException> getExceptionType() {
        return SystemErrorException.class;
    }
    
    /**
     * Gets a sanitized message for system errors.
     * Sensitive information is removed for security reasons.
     *
     * @param exception the system error exception
     * @return the sanitized message
     */
    private String getSanitizedMessage(SystemErrorException exception) {
        String message = exception.getMessage();
        
        // For certain error types, provide generic messages to avoid exposing system details
        String errorType = exception.getErrorType();
        switch (errorType) {
            case "OutOfMemoryError":
                return "System is experiencing memory constraints";
            case "StackOverflowError":
                return "System has encountered a stack overflow condition";
            default:
                return message != null ? message : "System error occurred";
        }
    }
    
    /**
     * Gets the stack trace as a string array.
     *
     * @param exception the exception
     * @return the stack trace
     */
    private String[] getStackTrace(SystemErrorException exception) {
        StackTraceElement[] elements = exception.getStackTrace();
        String[] stackTrace = new String[elements.length];
        for (int i = 0; i < elements.length; i++) {
            stackTrace[i] = elements[i].toString();
        }
        return stackTrace;
    }
}
