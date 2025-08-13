package com.sdk.exceptions.checked;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DatabaseException.
 */
class DatabaseExceptionTest {
    
    @Test
    void testConstructorWithMessage() {
        String message = "Database connection failed";
        DatabaseException exception = new DatabaseException(message);
        
        assertEquals(message, exception.getMessage());
        assertEquals("DATABASE_ERROR", exception.getErrorCode());
        assertEquals("HIGH", exception.getSeverity());
        assertEquals("CHECKED", exception.getCategory());
        assertNotNull(exception.getErrorId());
    }
    
    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Database query failed";
        SQLException cause = new SQLException("Connection timeout");
        DatabaseException exception = new DatabaseException(message, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals("DATABASE_ERROR", exception.getErrorCode());
        assertEquals("HIGH", exception.getSeverity());
    }
    
    @Test
    void testConnectionFailureFactory() {
        String connectionUrl = "jdbc:postgresql://localhost:5432/testdb";
        SQLException cause = new SQLException("Connection refused");
        
        DatabaseException exception = DatabaseException.connectionFailure(connectionUrl, cause);
        
        assertTrue(exception.getMessage().contains("Failed to connect to database"));
        assertTrue(exception.getMessage().contains(connectionUrl));
        assertEquals(cause, exception.getCause());
        assertEquals(connectionUrl, exception.getContext().getContextData("connectionUrl"));
        assertEquals("connection", exception.getContext().getMetadata("operationType"));
    }
    
    @Test
    void testQueryFailureFactory() {
        String query = "SELECT * FROM users WHERE id = ?";
        String sqlErrorCode = "42P01";
        SQLException cause = new SQLException("Table does not exist");
        
        DatabaseException exception = DatabaseException.queryFailure(query, sqlErrorCode, cause);
        
        assertTrue(exception.getMessage().contains("Query execution failed"));
        assertTrue(exception.getMessage().contains(sqlErrorCode));
        assertTrue(exception.getMessage().contains(query));
        assertEquals(cause, exception.getCause());
        assertEquals(query, exception.getContext().getContextData("query"));
        assertEquals(sqlErrorCode, exception.getContext().getContextData("sqlErrorCode"));
        assertEquals("query", exception.getContext().getMetadata("operationType"));
    }
    
    @Test
    void testTransactionFailureFactory() {
        String operation = "commit";
        String transactionId = "tx-12345";
        SQLException cause = new SQLException("Deadlock detected");
        
        DatabaseException exception = DatabaseException.transactionFailure(operation, transactionId, cause);
        
        assertTrue(exception.getMessage().contains("Transaction commit failed"));
        assertTrue(exception.getMessage().contains(transactionId));
        assertEquals(cause, exception.getCause());
        assertEquals(operation, exception.getContext().getContextData("operation"));
        assertEquals(transactionId, exception.getContext().getContextData("transactionId"));
        assertEquals("transaction", exception.getContext().getMetadata("operationType"));
    }
    
    @Test
    void testSeverityOverride() {
        DatabaseException exception = new DatabaseException("Test message");
        assertEquals("HIGH", exception.getSeverity());
    }
    
    @Test
    void testDefaultErrorCode() {
        DatabaseException exception = new DatabaseException("Test message");
        assertEquals("DATABASE_ERROR", exception.getErrorCode());
    }
    
    @Test
    void testToString() {
        DatabaseException exception = new DatabaseException("Database error");
        String toString = exception.toString();
        
        assertTrue(toString.contains("DatabaseException"));
        assertTrue(toString.contains("DATABASE_ERROR"));
        assertTrue(toString.contains("Database error"));
        assertTrue(toString.contains(exception.getErrorId()));
    }
}