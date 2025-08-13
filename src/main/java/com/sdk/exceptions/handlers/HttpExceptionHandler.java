package com.sdk.exceptions.handlers;

import com.sdk.exceptions.http.HttpException;
import com.sdk.exceptions.model.ErrorResponse;
import com.sdk.exceptions.model.ExceptionDetails;
import com.sdk.exceptions.utils.ExceptionLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Handler for HTTP exceptions.
 * Provides standardized handling for HTTP errors with appropriate status codes.
 */
public class HttpExceptionHandler implements ExceptionHandler<HttpException> {
    
    private static final Logger logger = LoggerFactory.getLogger(HttpExceptionHandler.class);
    private final ExceptionLogger exceptionLogger;
    
    public HttpExceptionHandler() {
        this.exceptionLogger = new ExceptionLogger();
    }
    
    public HttpExceptionHandler(ExceptionLogger exceptionLogger) {
        this.exceptionLogger = exceptionLogger;
    }
    
    @Override
    public boolean canHandle(Throwable exception) {
        return exception instanceof HttpException;
    }
    
    @Override
    public ErrorResponse handle(HttpException exception) {
        // Log the exception based on severity
        if (exception.isServerError()) {
            exceptionLogger.logError(exception, "HTTP server error occurred");
        } else if (exception.isClientError()) {
            exceptionLogger.logWarning(exception, "HTTP client error occurred");
        } else {
            exceptionLogger.logInfo(exception, "HTTP exception occurred");
        }
        
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
        
        // Create error response with HTTP status information
        return ErrorResponse.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .details(details)
                .httpStatusCode(exception.getStatusCodeValue())
                .build();
    }
    
    @Override
    public int getPriority() {
        return 15; // High priority for HTTP exceptions
    }
    
    @Override
    public Class<HttpException> getExceptionType() {
        return HttpException.class;
    }
    
    /**
     * Gets the stack trace as a string array.
     *
     * @param exception the exception
     * @return the stack trace
     */
    private String[] getStackTrace(HttpException exception) {
        StackTraceElement[] elements = exception.getStackTrace();
        String[] stackTrace = new String[elements.length];
        for (int i = 0; i < elements.length; i++) {
            stackTrace[i] = elements[i].toString();
        }
        return stackTrace;
    }
}
