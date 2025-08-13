package com.sdk.exceptions.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ExceptionHandlingConfigBuilder.
 */
class ExceptionHandlingConfigBuilderTest {
    
    @Test
    void testDevelopmentConfiguration() {
        ExceptionHandlingConfig config = ExceptionHandlingConfigBuilder
                .forDevelopment()
                .build();
        
        assertTrue(config.isIncludeStackTrace());
        assertTrue(config.isIncludeExceptionDetails());
        assertTrue(config.isLogExceptions());
        assertEquals("DEBUG", config.getLogLevel());
        assertFalse(config.isSanitizeMessages());
        assertEquals(Integer.MAX_VALUE, config.getMaxStackTraceElements());
    }
    
    @Test
    void testProductionConfiguration() {
        ExceptionHandlingConfig config = ExceptionHandlingConfigBuilder
                .forProduction()
                .build();
        
        assertFalse(config.isIncludeStackTrace());
        assertFalse(config.isIncludeExceptionDetails());
        assertTrue(config.isLogExceptions());
        assertEquals("ERROR", config.getLogLevel());
        assertTrue(config.isSanitizeMessages());
        assertEquals(10, config.getMaxStackTraceElements());
    }
    
    @Test
    void testTestingConfiguration() {
        ExceptionHandlingConfig config = ExceptionHandlingConfigBuilder
                .forTesting()
                .build();
        
        assertTrue(config.isIncludeStackTrace());
        assertTrue(config.isIncludeExceptionDetails());
        assertTrue(config.isLogExceptions());
        assertEquals("WARN", config.getLogLevel());
        assertFalse(config.isSanitizeMessages());
        assertFalse(config.isEnableCaching());
    }
    
    @Test
    void testHighSecurityConfiguration() {
        ExceptionHandlingConfig config = ExceptionHandlingConfigBuilder
                .forDevelopment()
                .forHighSecurity()
                .build();
        
        assertFalse(config.isIncludeStackTrace());
        assertFalse(config.isIncludeExceptionDetails());
        assertTrue(config.isSanitizeMessages());
        assertTrue(config.isLogExceptions());
        assertEquals("ERROR", config.getLogLevel());
    }
    
    @Test
    void testDebuggingConfiguration() {
        ExceptionHandlingConfig config = ExceptionHandlingConfigBuilder
                .forProduction()
                .forDebugging()
                .build();
        
        assertTrue(config.isIncludeStackTrace());
        assertTrue(config.isIncludeExceptionDetails());
        assertFalse(config.isSanitizeMessages());
        assertTrue(config.isLogExceptions());
        assertEquals("DEBUG", config.getLogLevel());
        assertEquals(Integer.MAX_VALUE, config.getMaxStackTraceElements());
    }
    
    @Test
    void testCustomConfiguration() {
        ExceptionHandlingConfig config = new ExceptionHandlingConfigBuilder()
                .withStackTraces()
                .withDetailedExceptions()
                .withLogging()
                .withLogLevel("INFO")
                .withMessageSanitization()
                .withLimitedStackTrace(15)
                .withCaching(false)
                .withDateTimeFormat("yyyy-MM-dd HH:mm:ss")
                .build();
        
        assertTrue(config.isIncludeStackTrace());
        assertTrue(config.isIncludeExceptionDetails());
        assertTrue(config.isLogExceptions());
        assertEquals("INFO", config.getLogLevel());
        assertTrue(config.isSanitizeMessages());
        assertEquals(15, config.getMaxStackTraceElements());
        assertFalse(config.isEnableCaching());
        assertEquals("yyyy-MM-dd HH:mm:ss", config.getDateTimeFormat());
    }
    
    @Test
    void testFluentChaining() {
        ExceptionHandlingConfig config = new ExceptionHandlingConfigBuilder()
                .withStackTraces()
                .withDetailedExceptions()
                .withLogging()
                .withLogLevel("WARN")
                .withoutMessageSanitization()
                .withUnlimitedStackTrace()
                .withCaching(true)
                .build();
        
        assertTrue(config.isIncludeStackTrace());
        assertTrue(config.isIncludeExceptionDetails());
        assertTrue(config.isLogExceptions());
        assertEquals("WARN", config.getLogLevel());
        assertFalse(config.isSanitizeMessages());
        assertEquals(Integer.MAX_VALUE, config.getMaxStackTraceElements());
        assertTrue(config.isEnableCaching());
    }
    
    @Test
    void testNegativeConfigurationMethods() {
        ExceptionHandlingConfig config = new ExceptionHandlingConfigBuilder()
                .withoutStackTraces()
                .withoutDetailedExceptions()
                .withoutLogging()
                .withoutMessageSanitization()
                .build();
        
        assertFalse(config.isIncludeStackTrace());
        assertFalse(config.isIncludeExceptionDetails());
        assertFalse(config.isLogExceptions());
        assertFalse(config.isSanitizeMessages());
    }
    
    @Test
    void testBuildingFromExistingConfig() {
        ExceptionHandlingConfig baseConfig = ExceptionHandlingConfig.developmentConfig();
        
        ExceptionHandlingConfig derivedConfig = new ExceptionHandlingConfigBuilder(baseConfig)
                .withoutStackTraces()
                .withLogLevel("ERROR")
                .build();
        
        // Should inherit most settings from base config
        assertTrue(derivedConfig.isIncludeExceptionDetails());
        assertTrue(derivedConfig.isLogExceptions());
        
        // But override specific settings
        assertFalse(derivedConfig.isIncludeStackTrace());
        assertEquals("ERROR", derivedConfig.getLogLevel());
    }
    
    @Test
    void testDefaultValues() {
        ExceptionHandlingConfig config = new ExceptionHandlingConfigBuilder().build();
        
        // Verify default values match ExceptionHandlingConfig.Builder defaults
        assertFalse(config.isIncludeStackTrace());
        assertTrue(config.isIncludeExceptionDetails());
        assertTrue(config.isLogExceptions());
        assertEquals("ERROR", config.getLogLevel());
        assertTrue(config.isSanitizeMessages());
        assertEquals(20, config.getMaxStackTraceElements());
        assertTrue(config.isEnableCaching());
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS", config.getDateTimeFormat());
    }
    
    @Test
    void testCustomDateTimeFormat() {
        String customFormat = "dd/MM/yyyy HH:mm:ss";
        ExceptionHandlingConfig config = new ExceptionHandlingConfigBuilder()
                .withDateTimeFormat(customFormat)
                .build();
        
        assertEquals(customFormat, config.getDateTimeFormat());
    }
    
    @Test
    void testStackTraceLimits() {
        ExceptionHandlingConfig configWithLimit = new ExceptionHandlingConfigBuilder()
                .withLimitedStackTrace(5)
                .build();
        
        ExceptionHandlingConfig configUnlimited = new ExceptionHandlingConfigBuilder()
                .withUnlimitedStackTrace()
                .build();
        
        assertEquals(5, configWithLimit.getMaxStackTraceElements());
        assertEquals(Integer.MAX_VALUE, configUnlimited.getMaxStackTraceElements());
    }
    
    @Test
    void testLogLevelSettings() {
        String[] logLevels = {"TRACE", "DEBUG", "INFO", "WARN", "ERROR"};
        
        for (String level : logLevels) {
            ExceptionHandlingConfig config = new ExceptionHandlingConfigBuilder()
                    .withLogLevel(level)
                    .build();
            
            assertEquals(level, config.getLogLevel());
        }
    }
}
