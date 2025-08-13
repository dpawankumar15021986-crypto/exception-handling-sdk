package com.sdk.exceptions.unchecked;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ValidationException.
 */
class ValidationExceptionTest {
    
    @Test
    void testConstructorWithMessage() {
        String message = "Validation failed";
        ValidationException exception = new ValidationException(message);
        
        assertEquals(message, exception.getMessage());
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
        assertEquals("MEDIUM", exception.getSeverity());
        assertEquals("UNCHECKED", exception.getCategory());
        assertNotNull(exception.getErrorId());
    }
    
    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Validation failed";
        RuntimeException cause = new IllegalArgumentException("Invalid input");
        ValidationException exception = new ValidationException(message, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
    }
    
    @Test
    void testFieldValidationFactory() {
        String fieldName = "email";
        String invalidValue = "invalid-email";
        String reason = "Invalid email format";
        
        ValidationException exception = ValidationException.fieldValidation(fieldName, invalidValue, reason);
        
        assertTrue(exception.getMessage().contains("Field validation failed"));
        assertTrue(exception.getMessage().contains(fieldName));
        assertTrue(exception.getMessage().contains(reason));
        assertEquals(fieldName, exception.getContext().getContextData("fieldName"));
        assertEquals(invalidValue, exception.getContext().getContextData("invalidValue"));
        assertEquals(reason, exception.getContext().getContextData("validationError"));
        assertEquals("field", exception.getContext().getMetadata("validationType"));
    }
    
    @Test
    void testRequiredFieldFactory() {
        String fieldName = "username";
        
        ValidationException exception = ValidationException.requiredField(fieldName);
        
        assertTrue(exception.getMessage().contains("Required field missing"));
        assertTrue(exception.getMessage().contains(fieldName));
        assertEquals(fieldName, exception.getContext().getContextData("fieldName"));
        assertEquals("required", exception.getContext().getMetadata("validationType"));
    }
    
    @Test
    void testMultipleFieldsFactory() {
        List<String> fieldNames = Arrays.asList("email", "username", "password");
        
        ValidationException exception = ValidationException.multipleFields(fieldNames);
        
        assertTrue(exception.getMessage().contains("Multiple field validation failed"));
        assertTrue(exception.getMessage().contains("email, username, password"));
        assertEquals(fieldNames, exception.getContext().getContextData("fieldNames"));
        assertEquals("multiple", exception.getContext().getMetadata("validationType"));
    }
    
    @Test
    void testBusinessRuleFactory() {
        String ruleName = "AgeLimit";
        String ruleDescription = "User must be at least 18 years old";
        
        ValidationException exception = ValidationException.businessRule(ruleName, ruleDescription);
        
        assertTrue(exception.getMessage().contains("Business rule validation failed"));
        assertTrue(exception.getMessage().contains(ruleName));
        assertTrue(exception.getMessage().contains(ruleDescription));
        assertEquals(ruleName, exception.getContext().getContextData("ruleName"));
        assertEquals(ruleDescription, exception.getContext().getContextData("ruleDescription"));
        assertEquals("business-rule", exception.getContext().getMetadata("validationType"));
    }
    
    @Test
    void testSeverityOverride() {
        ValidationException exception = new ValidationException("Test message");
        assertEquals("MEDIUM", exception.getSeverity());
    }
    
    @Test
    void testDefaultErrorCode() {
        ValidationException exception = new ValidationException("Test message");
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
    }
    
    @Test
    void testToString() {
        ValidationException exception = new ValidationException("Validation error");
        String toString = exception.toString();
        
        assertTrue(toString.contains("ValidationException"));
        assertTrue(toString.contains("VALIDATION_ERROR"));
        assertTrue(toString.contains("Validation error"));
        assertTrue(toString.contains(exception.getErrorId()));
    }
    
    @Test
    void testEmptyFieldNamesList() {
        List<String> emptyList = Arrays.asList();
        
        ValidationException exception = ValidationException.multipleFields(emptyList);
        
        assertTrue(exception.getMessage().contains("Multiple field validation failed"));
        assertEquals(emptyList, exception.getContext().getContextData("fieldNames"));
    }
}