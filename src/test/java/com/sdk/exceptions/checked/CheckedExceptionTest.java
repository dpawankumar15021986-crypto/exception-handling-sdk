package com.sdk.exceptions.checked;

import com.sdk.exceptions.core.ExceptionContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CheckedException.
 */
class CheckedExceptionTest {
    
    private ExceptionContext testContext;
    
    @BeforeEach
    void setUp() {
        testContext = new ExceptionContext()
                .addContextData("userId", "12345")
                .addMetadata("component", "TestService");
    }
    
    @Test
    void testConstructorWithMessage() {
        String message = "Test checked exception";
        CheckedException exception = new CheckedException(message);
        
        assertEquals(message, exception.getMessage());
        assertNotNull(exception.getErrorId());
        assertNotNull(exception.getTimestamp());
        assertEquals("CHECKED", exception.getCategory());
        assertEquals("MEDIUM", exception.getSeverity());
        assertTrue(exception.getErrorCode().startsWith("CHECKED_EXCEPTION"));
    }
    
    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Test checked exception";
        RuntimeException cause = new RuntimeException("Root cause");
        CheckedException exception = new CheckedException(message, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertNotNull(exception.getErrorId());
        assertEquals("CHECKED", exception.getCategory());
    }
    
    @Test
    void testConstructorWithErrorCodeAndMessage() {
        String errorCode = "CUSTOM_CHECKED_ERROR";
        String message = "Test checked exception";
        CheckedException exception = new CheckedException(errorCode, message);
        
        assertEquals(message, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
        assertNotNull(exception.getErrorId());
    }
    
    @Test
    void testConstructorWithErrorCodeMessageAndCause() {
        String errorCode = "CUSTOM_CHECKED_ERROR";
        String message = "Test checked exception";
        RuntimeException cause = new RuntimeException("Root cause");
        CheckedException exception = new CheckedException(errorCode, message, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(cause, exception.getCause());
        assertNotNull(exception.getErrorId());
    }
    
    @Test
    void testConstructorWithAllParameters() {
        String errorCode = "CUSTOM_CHECKED_ERROR";
        String message = "Test checked exception";
        RuntimeException cause = new RuntimeException("Root cause");
        
        CheckedException exception = new CheckedException(errorCode, message, cause, testContext);
        
        assertEquals(message, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(cause, exception.getCause());
        assertEquals(testContext, exception.getContext());
        assertEquals("12345", exception.getContext().getContextData("userId"));
        assertEquals("TestService", exception.getContext().getMetadata("component"));
    }
    
    @Test
    void testCategoryAndSeverity() {
        CheckedException exception = new CheckedException("Test message");
        
        assertEquals("CHECKED", exception.getCategory());
        assertEquals("MEDIUM", exception.getSeverity());
    }
    
    @Test
    void testToString() {
        CheckedException exception = new CheckedException("CUSTOM_ERROR", "Test message");
        String toString = exception.toString();
        
        assertTrue(toString.contains("CheckedException"));
        assertTrue(toString.contains("CUSTOM_ERROR"));
        assertTrue(toString.contains("Test message"));
        assertTrue(toString.contains(exception.getErrorId()));
    }
}