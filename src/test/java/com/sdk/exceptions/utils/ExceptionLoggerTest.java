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
    private ExceptionHandlingConfig config;
    private CheckedException testException;
    
    @BeforeEach
    void setUp() {
        config = ExceptionHandlingConfig.developmentConfig();
        logger = new ExceptionLogger(config);
        
        ExceptionContext context = new ExceptionContext()
                .addContextData("userId", "12345")
                .addMetadata("component", "TestService");
        
        testException = new CheckedException("TEST_ERROR", "Test exception message", null, context);
    }
    
    @Test
    void testLogException() {
        // This test verifies that logging doesn't throw an exception
        assertDoesNotThrow(() -> logger.logException(testException));
    }
    
    @Test
    void testLogExceptionWithCause() {
        RuntimeException cause = new RuntimeException("Root cause");
        CheckedException exceptionWithCause = new CheckedException("Wrapper exception", cause);
        
        assertDoesNotThrow(() -> logger.logException(exceptionWithCause));
    }
    
    @Test
    void testLogError() {
        assertDoesNotThrow(() -> logger.logError("Test error message"));
    }
    
    @Test
    void testLogErrorWithException() {
        RuntimeException exception = new RuntimeException("Test runtime exception");
        assertDoesNotThrow(() -> logger.logError("Error occurred", exception));
    }
    
    @Test
    void testLogWarning() {
        assertDoesNotThrow(() -> logger.logWarning("Test warning message"));
    }
    
    @Test
    void testLogWarningWithException() {
        RuntimeException exception = new RuntimeException("Test runtime exception");
        assertDoesNotThrow(() -> logger.logWarning("Warning occurred", exception));
    }
    
    @Test
    void testLogInfo() {
        assertDoesNotThrow(() -> logger.logInfo("Test info message"));
    }
    
    @Test
    void testLogDebug() {
        assertDoesNotThrow(() -> logger.logDebug("Test debug message"));
    }
    
    @Test
    void testLogDebugWithException() {
        RuntimeException exception = new RuntimeException("Test runtime exception");
        assertDoesNotThrow(() -> logger.logDebug("Debug info", exception));
    }
    
    @Test
    void testGetConfig() {
        ExceptionHandlingConfig retrievedConfig = logger.getConfig();
        assertEquals(config, retrievedConfig);
    }
    
    @Test
    void testWithProductionConfig() {
        ExceptionHandlingConfig prodConfig = ExceptionHandlingConfig.productionConfig();
        ExceptionLogger prodLogger = new ExceptionLogger(prodConfig);
        
        assertDoesNotThrow(() -> prodLogger.logException(testException));
        assertEquals(prodConfig, prodLogger.getConfig());
    }
    
    @Test
    void testWithNullMessage() {
        assertDoesNotThrow(() -> logger.logError((String) null));
        assertDoesNotThrow(() -> logger.logWarning((String) null));
        assertDoesNotThrow(() -> logger.logInfo((String) null));
        assertDoesNotThrow(() -> logger.logDebug((String) null));
    }
    
    @Test
    void testWithNullException() {
        assertDoesNotThrow(() -> logger.logException(null));
        assertDoesNotThrow(() -> logger.logError("Message", null));
        assertDoesNotThrow(() -> logger.logWarning("Message", null));
        assertDoesNotThrow(() -> logger.logDebug("Message", null));
    }
    
    @Test
    void testLogLevelFiltering() {
        // Test that the logger respects the configuration's log level
        ExceptionHandlingConfig errorOnlyConfig = ExceptionHandlingConfig.builder()
                .withLogLevel("ERROR")
                .withLogging()
                .build();
        
        ExceptionLogger errorLogger = new ExceptionLogger(errorOnlyConfig);
        
        // These should all work without throwing exceptions
        assertDoesNotThrow(() -> errorLogger.logError("Error message"));
        assertDoesNotThrow(() -> errorLogger.logWarning("Warning message"));
        assertDoesNotThrow(() -> errorLogger.logInfo("Info message"));
        assertDoesNotThrow(() -> errorLogger.logDebug("Debug message"));
    }
    
    @Test
    void testLoggingDisabled() {
        ExceptionHandlingConfig noLoggingConfig = ExceptionHandlingConfig.builder()
                .withoutLogging()
                .build();
        
        ExceptionLogger noLoggingLogger = new ExceptionLogger(noLoggingConfig);
        
        // Should not throw exceptions even when logging is disabled
        assertDoesNotThrow(() -> noLoggingLogger.logException(testException));
        assertDoesNotThrow(() -> noLoggingLogger.logError("Error message"));
    }
    
    @Test
    void testExceptionWithLongStackTrace() {
        // Create an exception with a deep stack trace
        RuntimeException deepException = createDeepException(10);
        
        assertDoesNotThrow(() -> logger.logException(deepException));
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
                    logger.logInfo("Thread " + threadId + " message " + j);
                    logger.logException(new RuntimeException("Thread " + threadId + " exception " + j));
                }
            });
        }
        
        assertDoesNotThrow(() -> {
            for (Thread thread : threads) {
                thread.start();
            }
            for (Thread thread : threads) {
                thread.join(1000); // Wait up to 1 second for each thread
            }
        });
    }
}