package com.sdk.exceptions.http;

import com.sdk.exceptions.core.ExceptionContext;

/**
 * Base class for HTTP-related exceptions.
 * Provides standardized handling for HTTP errors with status codes and context.
 */
public class HttpException extends RuntimeException {
    
    public static final String CATEGORY = "HTTP";
    
    private final HttpStatusCode statusCode;
    private final String errorCode;
    private final String errorId;
    private final java.time.LocalDateTime timestamp;
    private final ExceptionContext context;
    
    /**
     * Constructs an HttpException with the specified status code and message.
     *
     * @param statusCode the HTTP status code
     * @param message the detail message
     */
    public HttpException(HttpStatusCode statusCode, String message) {
        this(statusCode, message, null, null);
    }
    
    /**
     * Constructs an HttpException with the specified status code, message, and cause.
     *
     * @param statusCode the HTTP status code
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public HttpException(HttpStatusCode statusCode, String message, Throwable cause) {
        this(statusCode, message, cause, null);
    }
    
    /**
     * Constructs an HttpException with all parameters.
     *
     * @param statusCode the HTTP status code
     * @param message the detail message
     * @param cause the cause of this exception
     * @param context additional context information
     */
    public HttpException(HttpStatusCode statusCode, String message, Throwable cause, ExceptionContext context) {
        super(String.format("[%d %s] %s", statusCode.getCode(), statusCode.getReasonPhrase(), message), cause);
        this.statusCode = statusCode;
        this.errorCode = generateErrorCode(statusCode);
        this.errorId = java.util.UUID.randomUUID().toString();
        this.timestamp = java.time.LocalDateTime.now();
        this.context = context != null ? context : createDefaultContext(statusCode);
    }
    
    /**
     * Constructs an HttpException from an integer status code.
     *
     * @param statusCode the HTTP status code as integer
     * @param message the detail message
     */
    public HttpException(int statusCode, String message) {
        this(HttpStatusCode.valueOfOrThrow(statusCode), message);
    }
    
    /**
     * Constructs an HttpException from an integer status code with cause.
     *
     * @param statusCode the HTTP status code as integer
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public HttpException(int statusCode, String message, Throwable cause) {
        this(HttpStatusCode.valueOfOrThrow(statusCode), message, cause);
    }
    
    /**
     * Generates an error code based on the HTTP status code.
     *
     * @param statusCode the HTTP status code
     * @return the generated error code
     */
    private String generateErrorCode(HttpStatusCode statusCode) {
        return String.format("HTTP_%d_%s", 
                statusCode.getCode(), 
                statusCode.getReasonPhrase().toUpperCase().replaceAll("\\s+", "_"));
    }
    
    /**
     * Creates default context for the HTTP status code.
     *
     * @param statusCode the HTTP status code
     * @return the default context
     */
    private ExceptionContext createDefaultContext(HttpStatusCode statusCode) {
        return new ExceptionContext()
                .addContextData("statusCode", statusCode.getCode())
                .addContextData("reasonPhrase", statusCode.getReasonPhrase())
                .addContextData("category", statusCode.getCategory())
                .addMetadata("httpCategory", CATEGORY)
                .addMetadata("severity", getSeverity());
    }
    
    /**
     * Gets the HTTP status code.
     *
     * @return the HTTP status code
     */
    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
    
    /**
     * Gets the HTTP status code as an integer.
     *
     * @return the HTTP status code
     */
    public int getStatusCodeValue() {
        return statusCode.getCode();
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
     * Gets the category of this exception.
     *
     * @return the exception category
     */
    public String getCategory() {
        return CATEGORY;
    }
    
    /**
     * Gets the severity level based on the HTTP status code category.
     *
     * @return the severity level
     */
    public String getSeverity() {
        if (statusCode.isClientError()) {
            return "MEDIUM";
        } else if (statusCode.isServerError()) {
            return "HIGH";
        } else {
            return "LOW";
        }
    }
    
    /**
     * Checks if this is a client error (4xx).
     *
     * @return true if client error, false otherwise
     */
    public boolean isClientError() {
        return statusCode.isClientError();
    }
    
    /**
     * Checks if this is a server error (5xx).
     *
     * @return true if server error, false otherwise
     */
    public boolean isServerError() {
        return statusCode.isServerError();
    }
    
    @Override
    public String toString() {
        return String.format("HttpException{statusCode=%s, errorCode='%s', errorId='%s', timestamp=%s, message='%s'}",
                statusCode, errorCode, errorId, timestamp, getMessage());
    }
}
