package com.sdk.exceptions.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Standard error response model for API responses.
 * Provides consistent structure for error information across the SDK.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    @JsonProperty("success")
    private final boolean success;
    
    @JsonProperty("timestamp")
    private final LocalDateTime timestamp;
    
    @JsonProperty("message")
    private final String message;
    
    @JsonProperty("details")
    private final ExceptionDetails details;
    
    @JsonProperty("httpStatusCode")
    private final Integer httpStatusCode;
    
    @JsonProperty("path")
    private final String path;
    
    @JsonProperty("correlationId")
    private final String correlationId;
    
    /**
     * Private constructor for builder pattern.
     */
    private ErrorResponse(Builder builder) {
        this.success = builder.success;
        this.timestamp = builder.timestamp;
        this.message = builder.message;
        this.details = builder.details;
        this.httpStatusCode = builder.httpStatusCode;
        this.path = builder.path;
        this.correlationId = builder.correlationId;
    }
    
    /**
     * Creates a new builder.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Creates a simple error response.
     *
     * @param message the error message
     * @return the error response
     */
    public static ErrorResponse error(String message) {
        return builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .message(message)
                .build();
    }
    
    /**
     * Creates an error response with HTTP status code.
     *
     * @param message the error message
     * @param httpStatusCode the HTTP status code
     * @return the error response
     */
    public static ErrorResponse error(String message, int httpStatusCode) {
        return builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .message(message)
                .httpStatusCode(httpStatusCode)
                .build();
    }
    
    /**
     * Creates an error response with details.
     *
     * @param details the exception details
     * @return the error response
     */
    public static ErrorResponse error(ExceptionDetails details) {
        return builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .details(details)
                .build();
    }
    
    /**
     * Gets the success flag.
     *
     * @return the success flag
     */
    public boolean isSuccess() {
        return success;
    }
    
    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    /**
     * Gets the error message.
     *
     * @return the error message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Gets the exception details.
     *
     * @return the exception details
     */
    public ExceptionDetails getDetails() {
        return details;
    }
    
    /**
     * Gets the HTTP status code.
     *
     * @return the HTTP status code
     */
    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }
    
    /**
     * Gets the request path.
     *
     * @return the request path
     */
    public String getPath() {
        return path;
    }
    
    /**
     * Gets the correlation ID.
     *
     * @return the correlation ID
     */
    public String getCorrelationId() {
        return correlationId;
    }
    
    /**
     * Checks if this response has exception details.
     *
     * @return true if details are present, false otherwise
     */
    public boolean hasDetails() {
        return details != null;
    }
    
    /**
     * Checks if this response has an HTTP status code.
     *
     * @return true if HTTP status code is present, false otherwise
     */
    public boolean hasHttpStatusCode() {
        return httpStatusCode != null;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponse that = (ErrorResponse) o;
        return success == that.success &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(message, that.message) &&
                Objects.equals(details, that.details) &&
                Objects.equals(httpStatusCode, that.httpStatusCode) &&
                Objects.equals(path, that.path) &&
                Objects.equals(correlationId, that.correlationId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(success, timestamp, message, details, httpStatusCode, path, correlationId);
    }
    
    @Override
    public String toString() {
        return String.format("ErrorResponse{success=%s, timestamp=%s, message='%s', httpStatusCode=%s}",
                success, timestamp, message, httpStatusCode);
    }
    
    /**
     * Builder for ErrorResponse.
     */
    public static class Builder {
        private boolean success = false;
        private LocalDateTime timestamp = LocalDateTime.now();
        private String message;
        private ExceptionDetails details;
        private Integer httpStatusCode;
        private String path;
        private String correlationId;
        
        /**
         * Sets the success flag.
         *
         * @param success the success flag
         * @return this builder
         */
        public Builder success(boolean success) {
            this.success = success;
            return this;
        }
        
        /**
         * Sets the timestamp.
         *
         * @param timestamp the timestamp
         * @return this builder
         */
        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        /**
         * Sets the error message.
         *
         * @param message the error message
         * @return this builder
         */
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        
        /**
         * Sets the exception details.
         *
         * @param details the exception details
         * @return this builder
         */
        public Builder details(ExceptionDetails details) {
            this.details = details;
            return this;
        }
        
        /**
         * Sets the HTTP status code.
         *
         * @param httpStatusCode the HTTP status code
         * @return this builder
         */
        public Builder httpStatusCode(Integer httpStatusCode) {
            this.httpStatusCode = httpStatusCode;
            return this;
        }
        
        /**
         * Sets the request path.
         *
         * @param path the request path
         * @return this builder
         */
        public Builder path(String path) {
            this.path = path;
            return this;
        }
        
        /**
         * Sets the correlation ID.
         *
         * @param correlationId the correlation ID
         * @return this builder
         */
        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }
        
        /**
         * Builds the error response.
         *
         * @return the error response
         */
        public ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }
}
