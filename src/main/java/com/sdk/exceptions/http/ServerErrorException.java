package com.sdk.exceptions.http;

import com.sdk.exceptions.core.ExceptionContext;

/**
 * Exception for HTTP server errors (5xx status codes).
 * Represents errors caused by server-side issues.
 */
public class ServerErrorException extends HttpException {
    
    /**
     * Constructs a ServerErrorException with the specified status code and message.
     *
     * @param statusCode the HTTP status code (must be 5xx)
     * @param message the detail message
     */
    public ServerErrorException(HttpStatusCode statusCode, String message) {
        super(validateServerErrorStatus(statusCode), message);
    }
    
    /**
     * Constructs a ServerErrorException with the specified status code, message, and cause.
     *
     * @param statusCode the HTTP status code (must be 5xx)
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public ServerErrorException(HttpStatusCode statusCode, String message, Throwable cause) {
        super(validateServerErrorStatus(statusCode), message, cause);
    }
    
    /**
     * Constructs a ServerErrorException with all parameters.
     *
     * @param statusCode the HTTP status code (must be 5xx)
     * @param message the detail message
     * @param cause the cause of this exception
     * @param context additional context information
     */
    public ServerErrorException(HttpStatusCode statusCode, String message, Throwable cause, ExceptionContext context) {
        super(validateServerErrorStatus(statusCode), message, cause, context);
    }
    
    /**
     * Creates an InternalServerError (500) exception.
     *
     * @param message the detail message
     * @return a ServerErrorException for internal server error
     */
    public static ServerErrorException internalServerError(String message) {
        return new ServerErrorException(HttpStatusCode.INTERNAL_SERVER_ERROR, message);
    }
    
    /**
     * Creates an InternalServerError (500) exception with cause.
     *
     * @param message the detail message
     * @param cause the underlying cause
     * @return a ServerErrorException for internal server error
     */
    public static ServerErrorException internalServerError(String message, Throwable cause) {
        return new ServerErrorException(HttpStatusCode.INTERNAL_SERVER_ERROR, message, cause);
    }
    
    /**
     * Creates an InternalServerError (500) exception with component context.
     *
     * @param component the component where the error occurred
     * @param operation the operation that failed
     * @param cause the underlying cause
     * @return a ServerErrorException for internal server error
     */
    public static ServerErrorException internalServerError(String component, String operation, Throwable cause) {
        String message = String.format("Internal error in %s during %s operation", component, operation);
        ExceptionContext context = new ExceptionContext()
                .addContextData("component", component)
                .addContextData("operation", operation)
                .addMetadata("errorType", "internal_error");
        return new ServerErrorException(HttpStatusCode.INTERNAL_SERVER_ERROR, message, cause, context);
    }
    
    /**
     * Creates a NotImplemented (501) exception.
     *
     * @param feature the feature that is not implemented
     * @return a ServerErrorException for not implemented
     */
    public static ServerErrorException notImplemented(String feature) {
        String message = String.format("Feature not implemented: %s", feature);
        ExceptionContext context = new ExceptionContext()
                .addContextData("feature", feature)
                .addMetadata("errorType", "not_implemented");
        return new ServerErrorException(HttpStatusCode.NOT_IMPLEMENTED, message, null, context);
    }
    
    /**
     * Creates a BadGateway (502) exception.
     *
     * @param upstreamService the upstream service that returned a bad response
     * @param cause the underlying cause
     * @return a ServerErrorException for bad gateway
     */
    public static ServerErrorException badGateway(String upstreamService, Throwable cause) {
        String message = String.format("Bad response from upstream service: %s", upstreamService);
        ExceptionContext context = new ExceptionContext()
                .addContextData("upstreamService", upstreamService)
                .addMetadata("errorType", "bad_gateway");
        return new ServerErrorException(HttpStatusCode.BAD_GATEWAY, message, cause, context);
    }
    
    /**
     * Creates a ServiceUnavailable (503) exception.
     *
     * @param service the service that is unavailable
     * @return a ServerErrorException for service unavailable
     */
    public static ServerErrorException serviceUnavailable(String service) {
        String message = String.format("Service unavailable: %s", service);
        ExceptionContext context = new ExceptionContext()
                .addContextData("service", service)
                .addMetadata("errorType", "service_unavailable");
        return new ServerErrorException(HttpStatusCode.SERVICE_UNAVAILABLE, message, null, context);
    }
    
    /**
     * Creates a ServiceUnavailable (503) exception with retry information.
     *
     * @param service the service that is unavailable
     * @param retryAfterSeconds seconds until service may be available
     * @return a ServerErrorException for service unavailable
     */
    public static ServerErrorException serviceUnavailable(String service, int retryAfterSeconds) {
        String message = String.format("Service unavailable: %s (retry after %d seconds)", service, retryAfterSeconds);
        ExceptionContext context = new ExceptionContext()
                .addContextData("service", service)
                .addContextData("retryAfterSeconds", retryAfterSeconds)
                .addMetadata("errorType", "service_unavailable");
        return new ServerErrorException(HttpStatusCode.SERVICE_UNAVAILABLE, message, null, context);
    }
    
    /**
     * Creates a GatewayTimeout (504) exception.
     *
     * @param upstreamService the upstream service that timed out
     * @param timeoutSeconds the timeout duration in seconds
     * @return a ServerErrorException for gateway timeout
     */
    public static ServerErrorException gatewayTimeout(String upstreamService, int timeoutSeconds) {
        String message = String.format("Gateway timeout waiting for %s after %d seconds", upstreamService, timeoutSeconds);
        ExceptionContext context = new ExceptionContext()
                .addContextData("upstreamService", upstreamService)
                .addContextData("timeoutSeconds", timeoutSeconds)
                .addMetadata("errorType", "gateway_timeout");
        return new ServerErrorException(HttpStatusCode.GATEWAY_TIMEOUT, message, null, context);
    }
    
    /**
     * Validates that the status code is a server error (5xx).
     *
     * @param statusCode the status code to validate
     * @return the validated status code
     * @throws IllegalArgumentException if the status code is not a server error
     */
    private static HttpStatusCode validateServerErrorStatus(HttpStatusCode statusCode) {
        if (!statusCode.isServerError()) {
            throw new IllegalArgumentException(
                    String.format("Status code %d is not a server error (5xx)", statusCode.getCode()));
        }
        return statusCode;
    }
}
