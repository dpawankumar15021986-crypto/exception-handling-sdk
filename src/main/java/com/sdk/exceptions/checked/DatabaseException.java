package com.sdk.exceptions.checked;

import com.sdk.exceptions.core.ExceptionContext;

/**
 * Exception thrown when database operations fail.
 * Wraps SQLException and provides additional context.
 */
public class DatabaseException extends CheckedException {
    
    public static final String DEFAULT_ERROR_CODE = "DATABASE_ERROR";
    public static final String SEVERITY = "HIGH";
    
    /**
     * Constructs a DatabaseException with the specified message.
     *
     * @param message the detail message
     */
    public DatabaseException(String message) {
        super(DEFAULT_ERROR_CODE, message);
    }
    
    /**
     * Constructs a DatabaseException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public DatabaseException(String message, Throwable cause) {
        super(DEFAULT_ERROR_CODE, message, cause);
    }
    
    /**
     * Constructs a DatabaseException for connection issues.
     *
     * @param connectionUrl the database connection URL
     * @param cause the underlying cause
     */
    public static DatabaseException connectionFailure(String connectionUrl, Throwable cause) {
        return new DatabaseException(DEFAULT_ERROR_CODE,
              String.format("Failed to connect to database: %s", connectionUrl),
              cause,
              new ExceptionContext()
                  .addContextData("connectionUrl", connectionUrl)
                  .addMetadata("operationType", "connection"));
    }
    
    /**
     * Constructs a DatabaseException for query execution issues.
     *
     * @param query the SQL query that failed
     * @param sqlErrorCode the SQL error code
     * @param cause the underlying cause
     */
    public static DatabaseException queryFailure(String query, String sqlErrorCode, Throwable cause) {
        return new DatabaseException(DEFAULT_ERROR_CODE,
              String.format("Query execution failed (SQL Error Code: %s): %s", sqlErrorCode, query),
              cause,
              new ExceptionContext()
                  .addContextData("query", query)
                  .addContextData("sqlErrorCode", sqlErrorCode)
                  .addMetadata("operationType", "query"));
    }
    
    /**
     * Constructs a DatabaseException for transaction issues.
     *
     * @param operation the transaction operation (commit, rollback, etc.)
     * @param transactionId the transaction identifier
     * @param cause the underlying cause
     */
    public static DatabaseException transactionFailure(String operation, String transactionId, Throwable cause) {
        return new DatabaseException(DEFAULT_ERROR_CODE,
              String.format("Transaction %s failed for transaction ID: %s", operation, transactionId),
              cause,
              new ExceptionContext()
                  .addContextData("operation", operation)
                  .addContextData("transactionId", transactionId)
                  .addMetadata("operationType", "transaction"));
    }
    
    @Override
    public String getSeverity() {
        return SEVERITY;
    }
}
