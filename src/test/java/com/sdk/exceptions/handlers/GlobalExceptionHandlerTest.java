package com.sdk.exceptions.handlers;

import com.sdk.exceptions.checked.CheckedException;
import com.sdk.exceptions.unchecked.UncheckedException;
import com.sdk.exceptions.error.SystemErrorException;
import com.sdk.exceptions.http.HttpException;
import com.sdk.exceptions.http.HttpStatusCode;
import com.sdk.exceptions.model.ErrorResponse;
import com.sdk.exceptions.config.ExceptionHandlingConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GlobalExceptionHandler.
 */
class GlobalExceptionHandlerTest {
    
    private GlobalExceptionHandler globalHandler;
    
    @BeforeEach
    void setUp() {
        ExceptionHandlingConfig config = ExceptionHandlingConfig.developmentConfig();
        globalHandler = new GlobalExceptionHandler(config);
    }
    
    @Test
    void testHandleCheckedException() {
        CheckedException exception = new CheckedException("Test checked exception");
        ErrorResponse response = globalHandler.handleException(exception);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getDetails());
        assertEquals(exception.getErrorCode(), response.getDetails().getErrorCode());
        assertEquals(exception.getMessage(), response.getDetails().getMessage());
        assertEquals("CHECKED", response.getDetails().getCategory());
    }
    
    @Test
    void testHandleUncheckedException() {
        UncheckedException exception = new UncheckedException("Test unchecked exception");
        ErrorResponse response = globalHandler.handleException(exception);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getDetails());
        assertEquals(exception.getErrorCode(), response.getDetails().getErrorCode());
        assertEquals(exception.getMessage(), response.getDetails().getMessage());
        assertEquals("UNCHECKED", response.getDetails().getCategory());
    }
    
    @Test
    void testHandleSystemError() {
        OutOfMemoryError error = new OutOfMemoryError("Test out of memory");
        SystemErrorException exception = new SystemErrorException(error);
        ErrorResponse response = globalHandler.handleException(exception);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getDetails());
        assertEquals("ERROR", response.getDetails().getCategory());
        assertEquals("CRITICAL", response.getDetails().getSeverity());
    }
    
    @Test
    void testHandleHttpException() {
        HttpException exception = new HttpException(HttpStatusCode.NOT_FOUND, "Resource not found");
        ErrorResponse response = globalHandler.handleException(exception);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getDetails());
        assertEquals("HTTP", response.getDetails().getCategory());
        assertEquals(404, response.getHttpStatusCode().intValue());
    }
    
    @Test
    void testHandleUnknownException() {
        IllegalStateException exception = new IllegalStateException("Unknown exception type");
        ErrorResponse response = globalHandler.handleException(exception);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getDetails());
        assertEquals("UNHANDLED_EXCEPTION", response.getDetails().getErrorCode());
        assertEquals("UNKNOWN", response.getDetails().getCategory());
        assertEquals("HIGH", response.getDetails().getSeverity());
    }
    
    @Test
    void testHandlerPriority() {
        // System errors should have higher priority than unchecked exceptions
        SystemErrorException systemError = new SystemErrorException(new OutOfMemoryError("test"));
        ErrorResponse response = globalHandler.handleException(systemError);
        
        assertEquals("ERROR", response.getDetails().getCategory());
        assertEquals("CRITICAL", response.getDetails().getSeverity());
    }
    
    @Test
    void testAddCustomHandler() {
        CustomExceptionHandler customHandler = new CustomExceptionHandler();
        globalHandler.addHandler(customHandler);
        
        CustomException exception = new CustomException("Custom exception");
        ErrorResponse response = globalHandler.handleException(exception);
        
        assertEquals("CUSTOM", response.getDetails().getCategory());
        assertEquals("HANDLED_BY_CUSTOM_HANDLER", response.getDetails().getErrorCode());
    }
    
    @Test
    void testRemoveHandler() {
        CustomExceptionHandler customHandler = new CustomExceptionHandler();
        globalHandler.addHandler(customHandler);
        globalHandler.removeHandler(customHandler);
        
        CustomException exception = new CustomException("Custom exception");
        ErrorResponse response = globalHandler.handleException(exception);
        
        // Should be handled as unknown exception since custom handler was removed
        assertEquals("UNKNOWN", response.getDetails().getCategory());
        assertEquals("UNHANDLED_EXCEPTION", response.getDetails().getErrorCode());
    }
    
    @Test
    void testHandlerCaching() {
        CheckedException exception1 = new CheckedException("First exception");
        CheckedException exception2 = new CheckedException("Second exception");
        
        // Both exceptions should be handled by the same handler
        ErrorResponse response1 = globalHandler.handleException(exception1);
        ErrorResponse response2 = globalHandler.handleException(exception2);
        
        assertEquals("CHECKED", response1.getDetails().getCategory());
        assertEquals("CHECKED", response2.getDetails().getCategory());
    }
    
    @Test
    void testClearCache() {
        CheckedException exception = new CheckedException("Test exception");
        globalHandler.handleException(exception); // Populate cache
        
        assertDoesNotThrow(() -> globalHandler.clearCache());
        
        // Should still work after cache clear
        ErrorResponse response = globalHandler.handleException(exception);
        assertEquals("CHECKED", response.getDetails().getCategory());
    }
    
    @Test
    void testGetHandlers() {
        int initialHandlerCount = globalHandler.getHandlers().size();
        
        CustomExceptionHandler customHandler = new CustomExceptionHandler();
        globalHandler.addHandler(customHandler);
        
        assertEquals(initialHandlerCount + 1, globalHandler.getHandlers().size());
        assertTrue(globalHandler.getHandlers().contains(customHandler));
    }
    
    @Test
    void testGetConfig() {
        ExceptionHandlingConfig config = globalHandler.getConfig();
        
        assertNotNull(config);
        assertTrue(config.isIncludeStackTrace()); // Development config includes stack trace
        assertTrue(config.isIncludeExceptionDetails());
    }
    
    @Test
    void testHandlingErrorDuringHandling() {
        // Create a handler that throws an exception
        ExceptionHandler<RuntimeException> faultyHandler = new ExceptionHandler<RuntimeException>() {
            @Override
            public boolean canHandle(Throwable exception) {
                return exception instanceof RuntimeException;
            }
            
            @Override
            public ErrorResponse handle(RuntimeException exception) {
                throw new RuntimeException("Handler failed");
            }
            
            @Override
            public Class<RuntimeException> getExceptionType() {
                return RuntimeException.class;
            }
            
            @Override
            public int getPriority() {
                return 1; // Highest priority
            }
        };
        
        globalHandler.addHandler(faultyHandler);
        
        RuntimeException exception = new RuntimeException("Original exception");
        ErrorResponse response = globalHandler.handleException(exception);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("EXCEPTION_HANDLING_ERROR", response.getDetails().getErrorCode());
        assertEquals("CRITICAL", response.getDetails().getSeverity());
    }
    
    /**
     * Custom exception for testing.
     */
    private static class CustomException extends Exception {
        public CustomException(String message) {
            super(message);
        }
    }
    
    /**
     * Custom exception handler for testing.
     */
    private static class CustomExceptionHandler implements ExceptionHandler<CustomException> {
        
        @Override
        public boolean canHandle(Throwable exception) {
            return exception instanceof CustomException;
        }
        
        @Override
        public ErrorResponse handle(CustomException exception) {
            return ErrorResponse.builder()
                    .success(false)
                    .details(com.sdk.exceptions.model.ExceptionDetails.builder()
                            .errorCode("HANDLED_BY_CUSTOM_HANDLER")
                            .message(exception.getMessage())
                            .category("CUSTOM")
                            .severity("LOW")
                            .build())
                    .build();
        }
        
        @Override
        public Class<CustomException> getExceptionType() {
            return CustomException.class;
        }
        
        @Override
        public int getPriority() {
            return 50;
        }
    }
}
