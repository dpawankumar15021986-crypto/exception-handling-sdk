package com.sdk.exceptions.http;

import com.sdk.exceptions.core.ExceptionContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HTTP exceptions.
 */
class HttpExceptionTest {
    
    @Test
    void testHttpExceptionWithStatusCodeAndMessage() {
        String message = "Resource not found";
        HttpException exception = new HttpException(HttpStatusCode.NOT_FOUND, message);
        
        assertEquals(HttpStatusCode.NOT_FOUND, exception.getStatusCode());
        assertEquals(404, exception.getStatusCodeValue());
        assertTrue(exception.getMessage().contains("404"));
        assertTrue(exception.getMessage().contains("Not Found"));
        assertTrue(exception.getMessage().contains(message));
        assertNotNull(exception.getErrorId());
        assertNotNull(exception.getTimestamp());
        assertEquals("HTTP", exception.getCategory());
    }
    
    @Test
    void testHttpExceptionWithIntegerStatusCode() {
        String message = "Bad request";
        HttpException exception = new HttpException(400, message);
        
        assertEquals(HttpStatusCode.BAD_REQUEST, exception.getStatusCode());
        assertEquals(400, exception.getStatusCodeValue());
    }
    
    @Test
    void testHttpExceptionWithCause() {
        String message = "Internal server error";
        RuntimeException cause = new RuntimeException("Database connection failed");
        HttpException exception = new HttpException(HttpStatusCode.INTERNAL_SERVER_ERROR, message, cause);
        
        assertEquals(cause, exception.getCause());
        assertEquals(HttpStatusCode.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }
    
    @Test
    void testHttpExceptionWithContext() {
        ExceptionContext context = new ExceptionContext()
                .addContextData("userId", "12345")
                .addMetadata("endpoint", "/api/users");
        
        HttpException exception = new HttpException(HttpStatusCode.FORBIDDEN, "Access denied", null, context);
        
        assertEquals(context, exception.getContext());
        assertEquals("12345", exception.getContext().getContextData("userId"));
        assertEquals("/api/users", exception.getContext().getMetadata("endpoint"));
    }
    
    @Test
    void testInvalidHttpStatusCodeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new HttpException(999, "Invalid status code");
        });
    }
    
    @Test
    void testClientErrorDetection() {
        HttpException clientError = new HttpException(HttpStatusCode.BAD_REQUEST, "Bad request");
        HttpException serverError = new HttpException(HttpStatusCode.INTERNAL_SERVER_ERROR, "Server error");
        HttpException successStatus = new HttpException(HttpStatusCode.OK, "OK");
        
        assertTrue(clientError.isClientError());
        assertFalse(clientError.isServerError());
        
        assertFalse(serverError.isClientError());
        assertTrue(serverError.isServerError());
        
        assertFalse(successStatus.isClientError());
        assertFalse(successStatus.isServerError());
    }
    
    @Test
    void testSeverityLevels() {
        HttpException clientError = new HttpException(HttpStatusCode.BAD_REQUEST, "Bad request");
        HttpException serverError = new HttpException(HttpStatusCode.INTERNAL_SERVER_ERROR, "Server error");
        HttpException redirectError = new HttpException(HttpStatusCode.MOVED_PERMANENTLY, "Moved");
        
        assertEquals("MEDIUM", clientError.getSeverity());
        assertEquals("HIGH", serverError.getSeverity());
        assertEquals("LOW", redirectError.getSeverity());
    }
    
    @Test
    void testErrorCodeGeneration() {
        HttpException notFoundError = new HttpException(HttpStatusCode.NOT_FOUND, "Not found");
        HttpException serverError = new HttpException(HttpStatusCode.INTERNAL_SERVER_ERROR, "Server error");
        
        assertEquals("HTTP_404_NOT_FOUND", notFoundError.getErrorCode());
        assertEquals("HTTP_500_INTERNAL_SERVER_ERROR", serverError.getErrorCode());
    }
    
    @Test
    void testToString() {
        HttpException exception = new HttpException(HttpStatusCode.BAD_REQUEST, "Invalid input");
        String toString = exception.toString();
        
        assertTrue(toString.contains("HttpException"));
        assertTrue(toString.contains("400"));
        assertTrue(toString.contains(exception.getErrorId()));
        assertTrue(toString.contains("HTTP_400_BAD_REQUEST"));
    }
    
    @Test
    void testClientErrorExceptionCreation() {
        // Test BadRequest creation
        ClientErrorException badRequest = ClientErrorException.badRequest("Invalid parameter");
        assertEquals(HttpStatusCode.BAD_REQUEST, badRequest.getStatusCode());
        
        // Test with field validation
        ClientErrorException badRequestWithField = ClientErrorException.badRequest("Invalid email", "email", "invalid-email");
        assertEquals("invalid-email", badRequestWithField.getContext().getContextData("invalidValue"));
        
        // Test Unauthorized
        ClientErrorException unauthorized = ClientErrorException.unauthorized("Invalid credentials");
        assertEquals(HttpStatusCode.UNAUTHORIZED, unauthorized.getStatusCode());
        
        // Test Forbidden
        ClientErrorException forbidden = ClientErrorException.forbidden("Access denied");
        assertEquals(HttpStatusCode.FORBIDDEN, forbidden.getStatusCode());
        
        // Test NotFound
        ClientErrorException notFound = ClientErrorException.notFound("User", "123");
        assertEquals(HttpStatusCode.NOT_FOUND, notFound.getStatusCode());
        assertTrue(notFound.getMessage().contains("User"));
        assertTrue(notFound.getMessage().contains("123"));
        
        // Test Conflict
        ClientErrorException conflict = ClientErrorException.conflict("Resource already exists");
        assertEquals(HttpStatusCode.CONFLICT, conflict.getStatusCode());
        
        // Test TooManyRequests
        ClientErrorException tooMany = ClientErrorException.tooManyRequests("Rate limit exceeded", 60);
        assertEquals(HttpStatusCode.TOO_MANY_REQUESTS, tooMany.getStatusCode());
        assertEquals(60, tooMany.getContext().getContextData("retryAfterSeconds"));
    }
    
    @Test
    void testServerErrorExceptionCreation() {
        // Test InternalServerError
        ServerErrorException internalError = ServerErrorException.internalServerError("Database error");
        assertEquals(HttpStatusCode.INTERNAL_SERVER_ERROR, internalError.getStatusCode());
        
        // Test with cause
        RuntimeException cause = new RuntimeException("Connection failed");
        ServerErrorException internalErrorWithCause = ServerErrorException.internalServerError("Service unavailable", cause);
        assertEquals(cause, internalErrorWithCause.getCause());
        
        // Test with component context
        ServerErrorException componentError = ServerErrorException.internalServerError("AuthService", "validateUser", cause);
        assertEquals("AuthService", componentError.getContext().getContextData("component"));
        assertEquals("validateUser", componentError.getContext().getContextData("operation"));
        
        // Test NotImplemented
        ServerErrorException notImplemented = ServerErrorException.notImplemented("Advanced search");
        assertEquals(HttpStatusCode.NOT_IMPLEMENTED, notImplemented.getStatusCode());
        assertTrue(notImplemented.getMessage().contains("Advanced search"));
        
        // Test BadGateway
        ServerErrorException badGateway = ServerErrorException.badGateway("PaymentService", cause);
        assertEquals(HttpStatusCode.BAD_GATEWAY, badGateway.getStatusCode());
        assertEquals("PaymentService", badGateway.getContext().getContextData("upstreamService"));
        
        // Test ServiceUnavailable
        ServerErrorException serviceUnavailable = ServerErrorException.serviceUnavailable("DatabaseService");
        assertEquals(HttpStatusCode.SERVICE_UNAVAILABLE, serviceUnavailable.getStatusCode());
        
        // Test ServiceUnavailable with retry
        ServerErrorException serviceUnavailableWithRetry = ServerErrorException.serviceUnavailable("DatabaseService", 30);
        assertEquals(30, serviceUnavailableWithRetry.getContext().getContextData("retryAfterSeconds"));
        
        // Test GatewayTimeout
        ServerErrorException gatewayTimeout = ServerErrorException.gatewayTimeout("ExternalAPI", 30);
        assertEquals(HttpStatusCode.GATEWAY_TIMEOUT, gatewayTimeout.getStatusCode());
        assertEquals("ExternalAPI", gatewayTimeout.getContext().getContextData("upstreamService"));
        assertEquals(30, gatewayTimeout.getContext().getContextData("timeoutSeconds"));
    }
    
    @Test
    void testClientErrorValidation() {
        // Should throw exception when trying to create ClientErrorException with non-4xx status
        assertThrows(IllegalArgumentException.class, () -> {
            new ClientErrorException(HttpStatusCode.INTERNAL_SERVER_ERROR, "Server error");
        });
    }
    
    @Test
    void testServerErrorValidation() {
        // Should throw exception when trying to create ServerErrorException with non-5xx status
        assertThrows(IllegalArgumentException.class, () -> {
            new ServerErrorException(HttpStatusCode.BAD_REQUEST, "Client error");
        });
    }
}
