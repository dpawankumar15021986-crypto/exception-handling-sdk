package com.sdk.exceptions.http;

import com.sdk.exceptions.core.ExceptionContext;

/**
 * Exception for HTTP client errors (4xx status codes).
 * Represents errors caused by client requests.
 */
public class ClientErrorException extends HttpException {
    
    /**
     * Constructs a ClientErrorException with the specified status code and message.
     *
     * @param statusCode the HTTP status code (must be 4xx)
     * @param message the detail message
     */
    public ClientErrorException(HttpStatusCode statusCode, String message) {
        super(validateClientErrorStatus(statusCode), message);
    }
    
    /**
     * Constructs a ClientErrorException with the specified status code, message, and cause.
     *
     * @param statusCode the HTTP status code (must be 4xx)
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public ClientErrorException(HttpStatusCode statusCode, String message, Throwable cause) {
        super(validateClientErrorStatus(statusCode), message, cause);
    }
    
    /**
     * Constructs a ClientErrorException with all parameters.
     *
     * @param statusCode the HTTP status code (must be 4xx)
     * @param message the detail message
     * @param cause the cause of this exception
     * @param context additional context information
     */
    public ClientErrorException(HttpStatusCode statusCode, String message, Throwable cause, ExceptionContext context) {
        super(validateClientErrorStatus(statusCode), message, cause, context);
    }
    
    /**
     * Creates a BadRequest (400) exception.
     *
     * @param message the detail message
     * @return a ClientErrorException for bad request
     */
    public static ClientErrorException badRequest(String message) {
        return new ClientErrorException(HttpStatusCode.BAD_REQUEST, message);
    }
    
    /**
     * Creates a BadRequest (400) exception with validation context.
     *
     * @param message the detail message
     * @param field the field that caused the error
     * @param value the invalid value
     * @return a ClientErrorException for bad request
     */
    public static ClientErrorException badRequest(String message, String field, Object value) {
        ExceptionContext context = new ExceptionContext()
                .addContextData("invalidField", field)
                .addContextData("invalidValue", value)
                .addMetadata("errorType", "validation");
        return new ClientErrorException(HttpStatusCode.BAD_REQUEST, message, null, context);
    }
    
    /**
     * Creates an Unauthorized (401) exception.
     *
     * @param message the detail message
     * @return a ClientErrorException for unauthorized access
     */
    public static ClientErrorException unauthorized(String message) {
        return new ClientErrorException(HttpStatusCode.UNAUTHORIZED, message);
    }
    
    /**
     * Creates an Unauthorized (401) exception with authentication context.
     *
     * @param message the detail message
     * @param authScheme the authentication scheme that failed
     * @return a ClientErrorException for unauthorized access
     */
    public static ClientErrorException unauthorized(String message, String authScheme) {
        ExceptionContext context = new ExceptionContext()
                .addContextData("authScheme", authScheme)
                .addMetadata("errorType", "authentication");
        return new ClientErrorException(HttpStatusCode.UNAUTHORIZED, message, null, context);
    }
    
    /**
     * Creates a Forbidden (403) exception.
     *
     * @param message the detail message
     * @return a ClientErrorException for forbidden access
     */
    public static ClientErrorException forbidden(String message) {
        return new ClientErrorException(HttpStatusCode.FORBIDDEN, message);
    }
    
    /**
     * Creates a Forbidden (403) exception with authorization context.
     *
     * @param message the detail message
     * @param resource the resource that was accessed
     * @param requiredPermission the required permission
     * @return a ClientErrorException for forbidden access
     */
    public static ClientErrorException forbidden(String message, String resource, String requiredPermission) {
        ExceptionContext context = new ExceptionContext()
                .addContextData("resource", resource)
                .addContextData("requiredPermission", requiredPermission)
                .addMetadata("errorType", "authorization");
        return new ClientErrorException(HttpStatusCode.FORBIDDEN, message, null, context);
    }
    
    /**
     * Creates a NotFound (404) exception.
     *
     * @param message the detail message
     * @return a ClientErrorException for not found
     */
    public static ClientErrorException notFound(String message) {
        return new ClientErrorException(HttpStatusCode.NOT_FOUND, message);
    }
    
    /**
     * Creates a NotFound (404) exception with resource context.
     *
     * @param resourceType the type of resource
     * @param resourceId the ID of the resource
     * @return a ClientErrorException for not found
     */
    public static ClientErrorException notFound(String resourceType, String resourceId) {
        String message = String.format("%s with ID '%s' not found", resourceType, resourceId);
        ExceptionContext context = new ExceptionContext()
                .addContextData("resourceType", resourceType)
                .addContextData("resourceId", resourceId)
                .addMetadata("errorType", "resource_not_found");
        return new ClientErrorException(HttpStatusCode.NOT_FOUND, message, null, context);
    }
    
    /**
     * Creates a Conflict (409) exception.
     *
     * @param message the detail message
     * @return a ClientErrorException for conflict
     */
    public static ClientErrorException conflict(String message) {
        return new ClientErrorException(HttpStatusCode.CONFLICT, message);
    }
    
    /**
     * Creates a TooManyRequests (429) exception.
     *
     * @param message the detail message
     * @param retryAfterSeconds seconds until retry is allowed
     * @return a ClientErrorException for too many requests
     */
    public static ClientErrorException tooManyRequests(String message, int retryAfterSeconds) {
        ExceptionContext context = new ExceptionContext()
                .addContextData("retryAfterSeconds", retryAfterSeconds)
                .addMetadata("errorType", "rate_limit");
        return new ClientErrorException(HttpStatusCode.TOO_MANY_REQUESTS, message, null, context);
    }
    
    /**
     * Validates that the status code is a client error (4xx).
     *
     * @param statusCode the status code to validate
     * @return the validated status code
     * @throws IllegalArgumentException if the status code is not a client error
     */
    private static HttpStatusCode validateClientErrorStatus(HttpStatusCode statusCode) {
        if (!statusCode.isClientError()) {
            throw new IllegalArgumentException(
                    String.format("Status code %d is not a client error (4xx)", statusCode.getCode()));
        }
        return statusCode;
    }
}
