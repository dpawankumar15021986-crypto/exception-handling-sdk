package com.sdk.exceptions.checked;

import com.sdk.exceptions.core.ExceptionContext;
import java.sql.SQLException;

/**
 * Exception for database-related errors.
 * Represents errors that occur during database operations.
 */
public class DatabaseException extends CheckedException {

    public static final String DEFAULT_ERROR_CODE = "DATABASE_ERROR";

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
     * Constructs a DatabaseException with the specified error code and message.
     *
     * @param errorCode the error code
     * @param message the detail message
     */
    public DatabaseException(String errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructs a DatabaseException with error code, message, and cause.
     *
     * @param errorCode the error code
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public DatabaseException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Constructs a DatabaseException with all parameters.
     *
     * @param errorCode the error code
     * @param message the detail message
     * @param cause the cause of this exception
     * @param context additional context information
     */
    public DatabaseException(String errorCode, String message, Throwable cause, ExceptionContext context) {
        super(errorCode, message, cause, context);
    }

    /**
     * Constructs a DatabaseException with error code, message and context.
     *
     * @param errorCode the error code
     * @param message the detail message
     * @param context additional context information
     */
    public DatabaseException(String errorCode, String message, ExceptionContext context) {
        super(errorCode, message, context);
    }

    @Override
    public String getSeverity() {
        return DEFAULT_SEVERITY;
    }

    /**
     * Factory method for connection failure exceptions.
     *
     * @param connectionUrl the connection URL
     * @param cause the original SQLException
     * @return DatabaseException instance
     */
    public static DatabaseException connectionFailure(String connectionUrl, SQLException cause) {
        ExceptionContext context = ExceptionContext.builder()
                .addContextData("connectionUrl", connectionUrl)
                .addMetadata("errorType", "connection")
                .build();

        return new DatabaseException("DB_CONNECTION_FAILED",
                "Failed to connect to database: " + connectionUrl, cause, context);
    }

    /**
     * Factory method for query failure exceptions.
     *
     * @param query the SQL query
     * @param sqlErrorCode the SQL error code
     * @param cause the original SQLException
     * @return DatabaseException instance
     */
    public static DatabaseException queryFailure(String query, String sqlErrorCode, SQLException cause) {
        ExceptionContext context = ExceptionContext.builder()
                .addContextData("query", query)
                .addContextData("sqlErrorCode", sqlErrorCode)
                .addMetadata("errorType", "query")
                .build();

        return new DatabaseException("DB_QUERY_FAILED",
                "Query execution failed: " + query, cause, context);
    }

    /**
     * Factory method for transaction failure exceptions.
     *
     * @param operation the transaction operation
     * @param transactionId the transaction ID
     * @param cause the original SQLException
     * @return DatabaseException instance
     */
    public static DatabaseException transactionFailure(String operation, String transactionId, SQLException cause) {
        ExceptionContext context = ExceptionContext.builder()
                .addContextData("operation", operation)
                .addContextData("transactionId", transactionId)
                .addMetadata("errorType", "transaction")
                .build();

        return new DatabaseException("DB_TRANSACTION_FAILED",
                "Transaction failed: " + operation, cause, context);
    }
}