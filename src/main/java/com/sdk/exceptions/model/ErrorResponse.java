
package com.sdk.exceptions.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Standardized error response model.
 * Provides consistent structure for all error responses in the SDK.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    private boolean success;
    private String message;
    private LocalDateTime timestamp;
    private ExceptionDetails details;
    private Integer httpStatusCode;
    
    private static final ObjectMapper objectMapper = createObjectMapper();
    
    /**
     * Default constructor.
     */
    public ErrorResponse() {
    }
    
    /**
     * Constructor with all parameters.
     *
     * @param success whether the operation was successful
     * @param message the response message
     * @param timestamp the timestamp
     * @param details the exception details
     * @param httpStatusCode the HTTP status code
     */
    public ErrorResponse(boolean success, String message, LocalDateTime timestamp, 
                        ExceptionDetails details, Integer httpStatusCode) {
        this.success = success;
        this.message = message;
        this.timestamp = timestamp;
        this.details = details;
        this.httpStatusCode = httpStatusCode;
    }
    
    /**
     * Creates a success response without a message.
     *
     * @return success response
     */
    public static ErrorResponse success() {
        return new ErrorResponse(true, "Success", LocalDateTime.now(), null, null);
    }
    
    /**
     * Creates a success response with a custom message.
     *
     * @param message the success message
     * @return success response
     */
    public static ErrorResponse success(String message) {
        return new ErrorResponse(true, message, LocalDateTime.now(), null, null);
    }
    
    /**
     * Creates an error response from exception details.
     *
     * @param details the exception details
     * @return error response
     */
    public static ErrorResponse error(ExceptionDetails details) {
        return new ErrorResponse(false, details.getMessage(), LocalDateTime.now(), details, null);
    }
    
    /**
     * Creates an error response with HTTP status code.
     *
     * @param details the exception details
     * @param httpStatusCode the HTTP status code
     * @return error response
     */
    public static ErrorResponse error(ExceptionDetails details, Integer httpStatusCode) {
        return new ErrorResponse(false, details.getMessage(), LocalDateTime.now(), details, httpStatusCode);
    }
    
    /**
     * Creates a builder for ErrorResponse.
     *
     * @return new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Converts this response to JSON string.
     *
     * @return JSON representation
     * @throws RuntimeException if serialization fails
     */
    public String toJsonString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize ErrorResponse to JSON", e);
        }
    }
    
    /**
     * Converts this response to a Map.
     *
     * @return Map representation
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("success", success);
        map.put("message", message);
        map.put("timestamp", timestamp);
        if (details != null) {
            map.put("details", details.toMap());
        }
        if (httpStatusCode != null) {
            map.put("httpStatusCode", httpStatusCode);
        }
        return map;
    }
    
    /**
     * Creates and configures ObjectMapper for JSON serialization.
     *
     * @return configured ObjectMapper
     */
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
    
    // Getters and setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public ExceptionDetails getDetails() {
        return details;
    }
    
    public void setDetails(ExceptionDetails details) {
        this.details = details;
    }
    
    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }
    
    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
    
    /**
     * Checks if this response has details.
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
    
    /**
     * Builder class for ErrorResponse.
     */
    public static class Builder {
        private boolean success;
        private String message;
        private LocalDateTime timestamp;
        private ExceptionDetails details;
        private Integer httpStatusCode;
        
        public Builder success(boolean success) {
            this.success = success;
            return this;
        }
        
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        
        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder details(ExceptionDetails details) {
            this.details = details;
            return this;
        }
        
        public Builder httpStatusCode(Integer httpStatusCode) {
            this.httpStatusCode = httpStatusCode;
            return this;
        }
        
        public ErrorResponse build() {
            return new ErrorResponse(success, message, timestamp, details, httpStatusCode);
        }
    }
}
