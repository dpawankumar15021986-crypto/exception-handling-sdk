package com.sdk.exceptions.unchecked;

import com.sdk.exceptions.core.ExceptionContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UncheckedException.
 */
class UncheckedExceptionTest {
    
    private ExceptionContext testContext;
    
    @BeforeEach
    void setUp() {
        testContext = new ExceptionContext()
                .addContextData("requestId", "req-12345")
                .addMetadata("service", "UserService");
    }
    
    @Test
    void testConstructorWithMessage() {
        String message = "Test unchecked exception";
        UncheckedException exception = new UncheckedException(message);
        
        assertEquals(message, exception.getMessage());
        assertNotNull(exception.getErrorId());
        assertNotNull(exception.getTimestamp());
        assertEquals("UNCHECKED", exception.getCategory());
        assertEquals("HIGH", exception.getSeverity());
        assertTrue(exception.getErrorCode().startsWith("UNCHECKED_EXCEPTION"));
    }
    
    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Test unchecked exception";
        RuntimeException cause = new NullPointerException("Null value");
        UncheckedException exception = new UncheckedException(message, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertNotNull(exception.getErrorId());
        assertEquals("UNCHECKED", exception.getCategory());
    }
    
    @Test
    void testConstructorWithErrorCodeAndMessage() {
        String errorCode = "CUSTOM_UNCHECKED_ERROR";
        String message = "Test unchecked exception";
        UncheckedException exception = new UncheckedException(errorCode, message);
        
        assertEquals(message, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
        assertNotNull(exception.getErrorId());
    }
    
    @Test
    void testConstructorWithErrorCodeMessageAndCause() {
        String errorCode = "CUSTOM_UNCHECKED_ERROR";
        String message = "Test unchecked exception";
        RuntimeException cause = new IllegalStateException("Invalid state");
        UncheckedException exception = new UncheckedException(errorCode, message, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(cause, exception.getCause());
        assertNotNull(exception.getErrorId());
    }
    
    @Test
    void testConstructorWithAllParameters() {
        String errorCode = "CUSTOM_UNCHECKED_ERROR";
        String message = "Test unchecked exception";
        RuntimeException cause = new IllegalArgumentException("Invalid argument");
        
        UncheckedException exception = new UncheckedException(errorCode, message, cause, testContext);
        
        assertEquals(message, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(cause, exception.getCause());
        assertEquals(testContext, exception.getContext());
        assertEquals("req-12345", exception.getContext().getContextData("requestId"));
        assertEquals("UserService", exception.getContext().getMetadata("service"));
    }
    
    @Test
    void testCategoryAndSeverity() {
        UncheckedException exception = new UncheckedException("Test message");
        
        assertEquals("UNCHECKED", exception.getCategory());
        assertEquals("HIGH", exception.getSeverity());
    }
    
    @Test
    void testToString() {
        UncheckedException exception = new UncheckedException("CUSTOM_ERROR", "Test message");
        String toString = exception.toString();
        
        assertTrue(toString.contains("UncheckedException"));
        assertTrue(toString.contains("CUSTOM_ERROR"));
        assertTrue(toString.contains("Test message"));
        assertTrue(toString.contains(exception.getErrorId()));
    }
    
    @Test
    void testNullMessageHandling() {
        UncheckedException exception = new UncheckedException((String) null);
        
        assertNull(exception.getMessage());
        assertNotNull(exception.getErrorId());
        assertEquals("UNCHECKED", exception.getCategory());
    }
    
    @Test
    void testUniqueErrorIds() {
        UncheckedException exception1 = new UncheckedException("message1");
        UncheckedException exception2 = new UncheckedException("message2");
        
        assertNotEquals(exception1.getErrorId(), exception2.getErrorId());
    }
}