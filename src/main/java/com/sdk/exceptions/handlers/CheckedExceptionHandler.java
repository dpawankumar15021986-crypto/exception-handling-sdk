package com.sdk.exceptions.handlers;

import com.sdk.exceptions.checked.CheckedException;
import com.sdk.exceptions.model.ErrorResponse;
import com.sdk.exceptions.model.ExceptionDetails;
import com.sdk.exceptions.utils.ExceptionLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Handler for checked exceptions.
 * Provides standardized handling for predictable errors.
 */
public class CheckedExceptionHandler implements ExceptionHandler<CheckedException> {
    
    private static final Logger logger = LoggerFactory.getLogger(CheckedExceptionHandler.class);
    private final ExceptionLogger exceptionLogger;
    
    public CheckedExceptionHandler() {
        this.exceptionLogger = new ExceptionLogger();
    }
    
    public CheckedExceptionHandler(ExceptionLogger exceptionLogger) {
        this.exceptionLogger = exceptionLogger;
    }
    
    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof CheckedException;
    }
    
    @Override
    public ErrorResponse handle(CheckedException exception) {
        // Log the exception
        exceptionLogger.logException(exception, "Checked exception occurred");
        
        // Create exception details
        ExceptionDetails details = ExceptionDetails.builder()
                .errorId(exception.getErrorId())
                .errorCode(exception.getErrorCode())
                .message(exception.getMessage())
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
                .build();
    }
    
    @Override
    public int getPriority() {
        return 20; // Higher priority for checked exceptions
    }
    
    @Override
    public Class<CheckedException> getExceptionType() {
        return CheckedException.class;
    }
    
    /**
     * Gets the stack trace as a string array.
     *
     * @param exception the exception
     * @return the stack trace
     */
    private String[] getStackTrace(CheckedException exception) {
        StackTraceElement[] elements = exception.getStackTrace();
        String[] stackTrace = new String[elements.length];
        for (int i = 0; i < elements.length; i++) {
            stackTrace[i] = elements[i].toString();
        }
        return stackTrace;
    }
}
