package com.sdk.exceptions.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BaseException.
 */
class BaseExceptionTest {
    
    private ExceptionContext testContext;
    
    @BeforeEach
    void setUp() {
        testContext = new ExceptionContext()
                .addContextData("testKey", "testValue")
                .addMetadata("testMeta", "testMetaValue");
    }
    
    @Test
    void testConstructorWithMessage() {
        String message = "Test exception message";
        TestBaseException exception = new TestBaseException(message);
        
        assertEquals(message, exception.getMessage());
        assertNotNull(exception.getErrorId());
        assertNotNull(exception.getTimestamp());
        assertNotNull(exception.getContext());
        assertEquals("TEST_BASE_EXCEPTION", exception.getErrorCode());
    }
    
    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Test exception message";
        RuntimeException cause = new RuntimeException("Cause exception");
        TestBaseException exception = new TestBaseException(message, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertNotNull(exception.getErrorId());
        assertNotNull(exception.getTimestamp());
    }
    
    @Test
    void testConstructorWithErrorCodeAndMessage() {
        String errorCode = "CUSTOM_ERROR_CODE";
        String message = "Test exception message";
        TestBaseException exception = new TestBaseException(errorCode, message);
        
        assertEquals(message, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
        assertNotNull(exception.getErrorId());
    }
    
    @Test
    void testConstructorWithAllParameters() {
        String errorCode = "CUSTOM_ERROR_CODE";
        String message = "Test exception message";
        RuntimeException cause = new RuntimeException("Cause exception");
        
        TestBaseException exception = new TestBaseException(errorCode, message, cause, testContext);
        
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(testContext, exception.getContext());
        assertNotNull(exception.getErrorId());
        assertNotNull(exception.getTimestamp());
    }
    
    @Test
    void testGenerateDefaultErrorCode() {
        TestBaseException exception = new TestBaseException("test message");
        assertEquals("TEST_BASE_EXCEPTION", exception.getErrorCode());
    }
    
    @Test
    void testTimestampGeneration() {
        LocalDateTime before = LocalDateTime.now();
        TestBaseException exception = new TestBaseException("test message");
        LocalDateTime after = LocalDateTime.now();
        
        assertTrue(exception.getTimestamp().isAfter(before.minusSeconds(1)));
        assertTrue(exception.getTimestamp().isBefore(after.plusSeconds(1)));
    }
    
    @Test
    void testUniqueErrorIds() {
        TestBaseException exception1 = new TestBaseException("message1");
        TestBaseException exception2 = new TestBaseException("message2");
        
        assertNotEquals(exception1.getErrorId(), exception2.getErrorId());
    }
    
    @Test
    void testEqualsAndHashCode() {
        TestBaseException exception1 = new TestBaseException("message");
        TestBaseException exception2 = new TestBaseException("message");
        
        // Exceptions should not be equal because they have different error IDs
        assertNotEquals(exception1, exception2);
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
        
        // Exception should be equal to itself
        assertEquals(exception1, exception1);
        assertEquals(exception1.hashCode(), exception1.hashCode());
    }
    
    @Test
    void testToString() {
        String message = "Test message";
        TestBaseException exception = new TestBaseException("CUSTOM_CODE", message);
        String toString = exception.toString();
        
        assertTrue(toString.contains("TestBaseException"));
        assertTrue(toString.contains("CUSTOM_CODE"));
        assertTrue(toString.contains(exception.getErrorId()));
        assertTrue(toString.contains(message));
    }
    
    @Test
    void testContextHandling() {
        TestBaseException exception = new TestBaseException("CUSTOM_CODE", "message", null, testContext);
        
        assertEquals(testContext, exception.getContext());
        assertEquals("testValue", exception.getContext().getContextData("testKey"));
        assertEquals("testMetaValue", exception.getContext().getMetadata("testMeta"));
    }
    
    @Test
    void testNullContextHandling() {
        TestBaseException exception = new TestBaseException("CUSTOM_CODE", "message", null, null);
        
        assertNotNull(exception.getContext());
        assertTrue(exception.getContext().getAllContextData().isEmpty());
        assertTrue(exception.getContext().getAllMetadata().isEmpty());
    }
    
    /**
     * Test implementation of BaseException for testing purposes.
     */
    private static class TestBaseException extends BaseException {
        
        public TestBaseException(String message) {
            super(message);
        }
        
        public TestBaseException(String message, Throwable cause) {
            super(message, cause);
        }
        
        public TestBaseException(String errorCode, String message) {
            super(errorCode, message);
        }
        
        public TestBaseException(String errorCode, String message, Throwable cause, ExceptionContext context) {
            super(errorCode, message, cause, context);
        }
        
        @Override
        public String getCategory() {
            return "TEST";
        }
        
        @Override
        public String getSeverity() {
            return "MEDIUM";
        }
    }
}
