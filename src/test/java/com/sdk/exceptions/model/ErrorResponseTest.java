package com.sdk.exceptions.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ErrorResponse.
 */
class ErrorResponseTest {
    
    private ExceptionDetails testDetails;
    
    @BeforeEach
    void setUp() {
        testDetails = ExceptionDetails.builder()
                .errorCode("TEST_ERROR")
                .message("Test error message")
                .category("TEST")
                .severity("MEDIUM")
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    @Test
    void testSuccessResponse() {
        ErrorResponse response = ErrorResponse.success();
        
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertNull(response.getDetails());
        assertNull(response.getHttpStatusCode());
        assertEquals("Success", response.getMessage());
    }
    
    @Test
    void testSuccessResponseWithMessage() {
        String message = "Operation completed successfully";
        ErrorResponse response = ErrorResponse.success(message);
        
        assertTrue(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getTimestamp());
    }
    
    @Test
    void testErrorResponse() {
        ErrorResponse response = ErrorResponse.error(testDetails);
        
        assertFalse(response.isSuccess());
        assertEquals(testDetails, response.getDetails());
        assertEquals("Test error message", response.getMessage());
        assertNotNull(response.getTimestamp());
    }
    
    @Test
    void testErrorResponseWithHttpStatus() {
        Integer httpStatus = 400;
        ErrorResponse response = ErrorResponse.error(testDetails, httpStatus);
        
        assertFalse(response.isSuccess());
        assertEquals(testDetails, response.getDetails());
        assertEquals(httpStatus, response.getHttpStatusCode());
        assertEquals("Test error message", response.getMessage());
    }
    
    @Test
    void testBuilderPattern() {
        Integer httpStatus = 500;
        String customMessage = "Custom error message";
        
        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .message(customMessage)
                .details(testDetails)
                .httpStatusCode(httpStatus)
                .timestamp(LocalDateTime.now())
                .build();
        
        assertFalse(response.isSuccess());
        assertEquals(customMessage, response.getMessage());
        assertEquals(testDetails, response.getDetails());
        assertEquals(httpStatus, response.getHttpStatusCode());
        assertNotNull(response.getTimestamp());
    }
    
    @Test
    void testToJsonString() {
        ErrorResponse response = ErrorResponse.error(testDetails);
        String json = response.toJsonString();
        
        assertNotNull(json);
        assertTrue(json.contains("\"success\":false"));
        assertTrue(json.contains("\"message\":\"Test error message\""));
        assertTrue(json.contains("\"details\""));
        assertTrue(json.contains("\"TEST_ERROR\""));
    }
    
    @Test
    void testToJsonStringWithNullDetails() {
        ErrorResponse response = ErrorResponse.success();
        String json = response.toJsonString();
        
        assertNotNull(json);
        assertTrue(json.contains("\"success\":true"));
        assertTrue(json.contains("\"message\":\"Success\""));
    }
    
    @Test
    void testToMap() {
        ErrorResponse response = ErrorResponse.error(testDetails, 400);
        Map<String, Object> map = response.toMap();
        
        assertNotNull(map);
        assertEquals(false, map.get("success"));
        assertEquals("Test error message", map.get("message"));
        assertEquals(400, map.get("httpStatusCode"));
        assertNotNull(map.get("timestamp"));
        assertNotNull(map.get("details"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> detailsMap = (Map<String, Object>) map.get("details");
        assertEquals("TEST_ERROR", detailsMap.get("errorCode"));
        assertEquals("Test error message", detailsMap.get("message"));
    }
    
    @Test
    void testToMapWithoutDetails() {
        ErrorResponse response = ErrorResponse.success("All good");
        Map<String, Object> map = response.toMap();
        
        assertNotNull(map);
        assertEquals(true, map.get("success"));
        assertEquals("All good", map.get("message"));
        assertNull(map.get("details"));
        assertNull(map.get("httpStatusCode"));
    }
    
    @Test
    void testHasDetails() {
        ErrorResponse responseWithDetails = ErrorResponse.error(testDetails);
        ErrorResponse responseWithoutDetails = ErrorResponse.success();
        
        assertTrue(responseWithDetails.hasDetails());
        assertFalse(responseWithoutDetails.hasDetails());
    }
    
    @Test
    void testHasHttpStatusCode() {
        ErrorResponse responseWithStatus = ErrorResponse.error(testDetails, 404);
        ErrorResponse responseWithoutStatus = ErrorResponse.error(testDetails);
        
        assertTrue(responseWithStatus.hasHttpStatusCode());
        assertFalse(responseWithoutStatus.hasHttpStatusCode());
    }
    
    @Test
    void testEqualsAndHashCode() {
        ErrorResponse response1 = ErrorResponse.error(testDetails);
        ErrorResponse response2 = ErrorResponse.error(testDetails);
        
        // Different timestamp means they should not be equal
        assertNotEquals(response1, response2);
        
        // Same instance should be equal
        assertEquals(response1, response1);
    }
    
    @Test
    void testToString() {
        ErrorResponse response = ErrorResponse.error(testDetails, 500);
        String toString = response.toString();
        
        assertTrue(toString.contains("ErrorResponse"));
        assertTrue(toString.contains("success=false"));
        assertTrue(toString.contains("httpStatusCode=500"));
        assertTrue(toString.contains("Test error message"));
    }
    
    @Test
    void testBuilderWithMinimalData() {
        ErrorResponse response = ErrorResponse.builder()
                .success(true)
                .build();
        
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertNull(response.getMessage());
        assertNull(response.getDetails());
    }
    
    @Test
    void testJsonSerializationFallback() {
        // Test with complex details that might cause serialization issues
        ErrorResponse response = ErrorResponse.error(testDetails);
        
        String json = response.toJsonString();
        assertNotNull(json);
        assertTrue(json.length() > 0);
        // Should not throw exception even if serialization encounters issues
    }
}