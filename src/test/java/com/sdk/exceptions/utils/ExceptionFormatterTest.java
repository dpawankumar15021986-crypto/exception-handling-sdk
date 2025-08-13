package com.sdk.exceptions.utils;

import com.sdk.exceptions.checked.CheckedException;
import com.sdk.exceptions.core.ExceptionContext;
import com.sdk.exceptions.http.HttpException;
import com.sdk.exceptions.http.HttpStatusCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ExceptionFormatter.
 */
class ExceptionFormatterTest {
    
    private ExceptionFormatter formatter;
    private CheckedException testException;
    private ExceptionContext testContext;
    
    @BeforeEach
    void setUp() {
        formatter = new ExceptionFormatter();
        testContext = new ExceptionContext()
                .addContextData("userId", "12345")
                .addContextData("operation", "getData")
                .addMetadata("component", "UserService");
        
        testException = new CheckedException("TEST_ERROR", "Test exception message", null, testContext);
    }
    
    @Test
    void testToJsonWithBasicException() {
        RuntimeException exception = new RuntimeException("Simple exception");
        String json = formatter.toJson(exception);
        
        assertNotNull(json);
        assertTrue(json.contains("RuntimeException"));
        assertTrue(json.contains("Simple exception"));
        assertTrue(json.contains("timestamp"));
        assertTrue(json.contains("stackTrace"));
    }
    
    @Test
    void testToJsonWithSDKException() {
        String json = formatter.toJson(testException);
        
        assertNotNull(json);
        assertTrue(json.contains("CheckedException"));
        assertTrue(json.contains("Test exception message"));
        assertTrue(json.contains("TEST_ERROR"));
        assertTrue(json.contains("12345")); // userId from context
        assertTrue(json.contains("UserService")); // metadata from context
    }
    
    @Test
    void testToJsonWithoutStackTrace() {
        String json = formatter.toJson(testException, false);
        
        assertNotNull(json);
        assertTrue(json.contains("Test exception message"));
        assertFalse(json.contains("stackTrace"));
    }
    
    @Test
    void testToMapConversion() {
        Map<String, Object> map = formatter.toMap(testException, true);
        
        assertNotNull(map);
        assertEquals("CheckedException", map.get("class"));
        assertEquals("Test exception message", map.get("message"));
        assertEquals("TEST_ERROR", map.get("errorCode"));
        assertEquals("CHECKED", map.get("category"));
        assertNotNull(map.get("timestamp"));
        assertNotNull(map.get("contextData"));
        assertNotNull(map.get("metadata"));
        assertNotNull(map.get("stackTrace"));
    }
    
    @Test
    void testToPlainText() {
        String plainText = formatter.toPlainText(testException);
        
        assertNotNull(plainText);
        assertTrue(plainText.contains("Exception: CheckedException"));
        assertTrue(plainText.contains("Message: Test exception message"));
        assertTrue(plainText.contains("Error ID:"));
        assertTrue(plainText.contains("Error Code: TEST_ERROR"));
        assertTrue(plainText.contains("Category: CHECKED"));
        assertTrue(plainText.contains("Stack Trace:"));
    }
    
    @Test
    void testToPlainTextWithoutStackTrace() {
        String plainText = formatter.toPlainText(testException, false);
        
        assertNotNull(plainText);
        assertTrue(plainText.contains("Exception: CheckedException"));
        assertTrue(plainText.contains("Message: Test exception message"));
        assertFalse(plainText.contains("Stack Trace:"));
    }
    
    @Test
    void testToXml() {
        String xml = formatter.toXml(testException);
        
        assertNotNull(xml);
        assertTrue(xml.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertTrue(xml.contains("<exception>"));
        assertTrue(xml.contains("<class>CheckedException</class>"));
        assertTrue(xml.contains("<message>Test exception message</message>"));
        assertTrue(xml.contains("<errorCode>TEST_ERROR</errorCode>"));
        assertTrue(xml.contains("<stackTrace>"));
        assertTrue(xml.contains("</exception>"));
    }
    
    @Test
    void testToXmlWithoutStackTrace() {
        String xml = formatter.toXml(testException, false);
        
        assertNotNull(xml);
        assertTrue(xml.contains("<class>CheckedException</class>"));
        assertFalse(xml.contains("<stackTrace>"));
    }
    
    @Test
    void testToLogFormat() {
        String logFormat = formatter.toLogFormat(testException);
        
        assertNotNull(logFormat);
        assertTrue(logFormat.contains("[CheckedException]"));
        assertTrue(logFormat.contains("Test exception message"));
        assertTrue(logFormat.contains("[errorId: " + testException.getErrorId() + "]"));
        assertTrue(logFormat.contains("[errorCode: TEST_ERROR]"));
    }
    
    @Test
    void testXmlEscaping() {
        CheckedException exceptionWithSpecialChars = new CheckedException("Error with <tags> & \"quotes\"");
        String xml = formatter.toXml(exceptionWithSpecialChars);
        
        assertTrue(xml.contains("&lt;tags&gt;"));
        assertTrue(xml.contains("&amp;"));
        assertTrue(xml.contains("&quot;quotes&quot;"));
    }
    
    @Test
    void testWithCauseException() {
        RuntimeException cause = new RuntimeException("Root cause");
        CheckedException exceptionWithCause = new CheckedException("Wrapper exception", cause);
        
        String json = formatter.toJson(exceptionWithCause);
        assertTrue(json.contains("cause"));
        assertTrue(json.contains("Root cause"));
        
        String xml = formatter.toXml(exceptionWithCause);
        assertTrue(xml.contains("<cause>"));
        assertTrue(xml.contains("Root cause"));
        
        String plainText = formatter.toPlainText(exceptionWithCause);
        assertTrue(plainText.contains("Caused by: RuntimeException: Root cause"));
    }
    
    @Test
    void testCustomDateTimeFormatter() {
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        ExceptionFormatter customFormatterInstance = new ExceptionFormatter(customFormatter);
        
        String json = customFormatterInstance.toJson(testException);
        assertNotNull(json);
        assertTrue(json.contains("timestamp"));
    }
    
    @Test
    void testHttpExceptionFormatting() {
        HttpException httpException = new HttpException(HttpStatusCode.NOT_FOUND, "Resource not found");
        String json = formatter.toJson(httpException);
        
        assertTrue(json.contains("HttpException"));
        assertTrue(json.contains("Resource not found"));
        assertTrue(json.contains("HTTP"));
    }
    
    @Test
    void testNullMessageHandling() {
        RuntimeException exceptionWithNullMessage = new RuntimeException((String) null);
        
        assertDoesNotThrow(() -> {
            formatter.toJson(exceptionWithNullMessage);
            formatter.toXml(exceptionWithNullMessage);
            formatter.toPlainText(exceptionWithNullMessage);
            formatter.toLogFormat(exceptionWithNullMessage);
        });
    }
    
    @Test
    void testEmptyContextHandling() {
        CheckedException exceptionWithEmptyContext = new CheckedException("Test message");
        
        Map<String, Object> map = formatter.toMap(exceptionWithEmptyContext, true);
        
        assertNotNull(map);
        assertEquals("CheckedException", map.get("class"));
        assertEquals("Test message", map.get("message"));
        // Should not include empty context or metadata
        assertFalse(map.containsKey("contextData") || 
                   (map.containsKey("contextData") && ((Map<?, ?>) map.get("contextData")).isEmpty()));
    }
    
    @Test
    void testJsonSerializationFallback() {
        // Create an exception that might cause JSON serialization issues
        CheckedException problematicException = new CheckedException("Test") {
            @Override
            public String getErrorId() {
                return "test-id";
            }
        };
        
        String json = formatter.toJson(problematicException);
        assertNotNull(json);
        assertTrue(json.length() > 0);
    }
}
