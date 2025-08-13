package com.sdk.exceptions.handlers;

import com.sdk.exceptions.unchecked.UncheckedException;
import com.sdk.exceptions.model.ErrorResponse;
import com.sdk.exceptions.model.ExceptionDetails;
import com.sdk.exceptions.utils.ExceptionLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Handler for unchecked exceptions.
 * Provides standardized handling for runtime errors.
 */
public class UncheckedExceptionHandler implements ExceptionHandler<UncheckedException> {
    
    private static final Logger logger = LoggerFactory.getLogger(UncheckedExceptionHandler.class);
    private final ExceptionLogger exceptionLogger;
    
    public UncheckedExceptionHandler() {
        this.exceptionLogger = new ExceptionLogger();
    }
    
    public UncheckedExceptionHandler(ExceptionLogger exceptionLogger) {
        this.exceptionLogger = exceptionLogger;
    }
    
    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof UncheckedException;
    }
    
    @Override
    public ErrorResponse handle(UncheckedException exception) {
        // Log the exception with higher severity for unchecked exceptions
        exceptionLogger.logException(exception, "Unchecked exception occurred - potential programming error");
        
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
        return 30; // Standard priority for unchecked exceptions
    }
    
    @Override
    public Class<UncheckedException> getExceptionType() {
        return UncheckedException.class;
    }
    
    /**
     * Gets the stack trace as a string array.
     *
     * @param exception the exception
     * @return the stack trace
     */
    private String[] getStackTrace(UncheckedException exception) {
        StackTraceElement[] elements = exception.getStackTrace();
        String[] stackTrace = new String[elements.length];
        for (int i = 0; i < elements.length; i++) {
            stackTrace[i] = elements[i].toString();
        }
        return stackTrace;
    }
}
