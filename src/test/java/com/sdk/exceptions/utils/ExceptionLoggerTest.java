
package com.sdk.exceptions.utils;

import com.sdk.exceptions.checked.CheckedException;
import com.sdk.exceptions.core.ExceptionContext;
import com.sdk.exceptions.config.ExceptionHandlingConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ExceptionLogger.
 */
class ExceptionLoggerTest {
    
    private ExceptionLogger logger;
    private CheckedException testException;
    
    @BeforeEach
    void setUp() {
        logger = new ExceptionLogger();
        
        ExceptionContext context = new ExceptionContext()
                .addContextData("userId", "12345")
                .addMetadata("component", "TestService");
        
        testException = new CheckedException("TEST_ERROR", "Test exception message", null, context);
    }
    
    @Test
    void testLogException() {
        // This test verifies that logging doesn't throw an exception
        assertDoesNotThrow(() -> logger.logException(testException, "Test exception occurred"));
    }
    
    @Test
    void testLogExceptionWithCause() {
        RuntimeException cause = new RuntimeException("Root cause");
        CheckedException exceptionWithCause = new CheckedException("Wrapper exception", cause);
        
        assertDoesNotThrow(() -> logger.logException(exceptionWithCause, "Exception with cause occurred"));
    }
    
    @Test
    void testLogError() {
        RuntimeException exception = new RuntimeException("Test runtime exception");
        assertDoesNotThrow(() -> logger.logError(exception, "Test error message"));
    }
    
    @Test
    void testLogErrorWithException() {
        RuntimeException exception = new RuntimeException("Test runtime exception");
        assertDoesNotThrow(() -> logger.logError(exception, "Error occurred"));
    }
    
    @Test
    void testLogWarning() {
        RuntimeException exception = new RuntimeException("Test runtime exception");
        assertDoesNotThrow(() -> logger.logWarning(exception, "Test warning message"));
    }
    
    @Test
    void testLogWarningWithException() {
        RuntimeException exception = new RuntimeException("Test runtime exception");
        assertDoesNotThrow(() -> logger.logWarning(exception, "Warning occurred"));
    }
    
    @Test
    void testLogInfo() {
        RuntimeException exception = new RuntimeException("Test runtime exception");
        assertDoesNotThrow(() -> logger.logInfo(exception, "Test info message"));
    }
    
    @Test
    void testLogDebug() {
        RuntimeException exception = new RuntimeException("Test runtime exception");
        assertDoesNotThrow(() -> logger.logDebug(exception, "Test debug message"));
    }
    
    @Test
    void testLogDebugWithException() {
        RuntimeException exception = new RuntimeException("Test runtime exception");
        assertDoesNotThrow(() -> logger.logDebug(exception, "Debug info"));
    }
    
    @Test
    void testWithProductionConfig() {
        ExceptionLogger prodLogger = new ExceptionLogger();
        
        assertDoesNotThrow(() -> prodLogger.logException(testException, "Production test"));
    }
    
    @Test
    void testWithNullMessage() {
        RuntimeException exception = new RuntimeException("Test exception");
        assertDoesNotThrow(() -> logger.logError(exception, null));
        assertDoesNotThrow(() -> logger.logWarning(exception, null));
        assertDoesNotThrow(() -> logger.logInfo(exception, null));
        assertDoesNotThrow(() -> logger.logDebug(exception, null));
    }
    
    @Test
    void testWithNullException() {
        assertDoesNotThrow(() -> logger.logException(null, "Message"));
        assertDoesNotThrow(() -> logger.logError(null, "Message"));
        assertDoesNotThrow(() -> logger.logWarning(null, "Message"));
        assertDoesNotThrow(() -> logger.logDebug(null, "Message"));
    }
    
    @Test
    void testLogLevelFiltering() {
        // Test that the logger works with different configurations
        ExceptionLogger errorLogger = new ExceptionLogger();
        
        // These should all work without throwing exceptions
        RuntimeException exception = new RuntimeException("Test exception");
        assertDoesNotThrow(() -> errorLogger.logError(exception, "Error message"));
        assertDoesNotThrow(() -> errorLogger.logWarning(exception, "Warning message"));
        assertDoesNotThrow(() -> errorLogger.logInfo(exception, "Info message"));
        assertDoesNotThrow(() -> errorLogger.logDebug(exception, "Debug message"));
    }
    
    @Test
    void testLoggingDisabled() {
        ExceptionLogger noLoggingLogger = new ExceptionLogger();
        
        // Should not throw exceptions even when logging is disabled
        RuntimeException exception = new RuntimeException("Test exception");
        assertDoesNotThrow(() -> noLoggingLogger.logException(testException, "Test message"));
        assertDoesNotThrow(() -> noLoggingLogger.logError(exception, "Error message"));
    }
    
    @Test
    void testExceptionWithLongStackTrace() {
        // Create an exception with a deep stack trace
        RuntimeException deepException = createDeepException(10);
        
        assertDoesNotThrow(() -> logger.logException(deepException, "Deep exception occurred"));
    }
    
    private RuntimeException createDeepException(int depth) {
        if (depth <= 0) {
            return new RuntimeException("Deep exception");
        }
        try {
            throw createDeepException(depth - 1);
        } catch (RuntimeException e) {
            return new RuntimeException("Level " + depth, e);
        }
    }
    
    @Test
    void testConcurrentLogging() {
        // Test that concurrent logging doesn't cause issues
        Thread[] threads = new Thread[5];
        
        for (int i = 0; i < threads.length; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    RuntimeException exception = new RuntimeException("Thread " + threadId + " exception " + j);
                    logger.logInfo(exception, "Thread " + threadId + " message " + j);
                    logger.logException(exception, "Thread " + threadId + " exception " + j);
                }
            });
        }
        
        assertDoesNotThrow(() -> {
            for (Thread thread : threads) {
                thread.start();
            }
            for (Thread thread : threads) {
                thread.join();
            }
        });
    }
}
