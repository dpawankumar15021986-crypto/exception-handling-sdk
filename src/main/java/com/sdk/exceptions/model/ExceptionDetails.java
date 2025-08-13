
package com.sdk.exceptions.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdk.exceptions.core.ExceptionContext;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

/**
 * Detailed information about an exception.
 * Contains comprehensive data for error reporting and debugging.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionDetails {
    
    private String errorId;
    private String errorCode;
    private String message;
    private String category;
    private String severity;
    private LocalDateTime timestamp;
    private ExceptionContext context;
    private String[] stackTrace;
    
    /**
     * Default constructor.
     */
    public ExceptionDetails() {
    }
    
    /**
     * Constructor with all parameters.
     */
    public ExceptionDetails(String errorId, String errorCode, String message, String category,
                           String severity, LocalDateTime timestamp, ExceptionContext context, 
                           String[] stackTrace) {
        this.errorId = errorId;
        this.errorCode = errorCode;
        this.message = message;
        this.category = category;
        this.severity = severity;
        this.timestamp = timestamp;
        this.context = context;
        this.stackTrace = stackTrace;
    }
    
    /**
     * Creates a builder for ExceptionDetails.
     *
     * @return new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Converts this object to a Map.
     *
     * @return Map representation
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("errorId", errorId);
        map.put("errorCode", errorCode);
        map.put("message", message);
        map.put("category", category);
        map.put("severity", severity);
        map.put("timestamp", timestamp);
        if (context != null) {
            map.put("context", context.getContextData());
        }
        if (stackTrace != null) {
            map.put("stackTrace", Arrays.asList(stackTrace));
        }
        return map;
    }
    
    // Getters and setters
    public String getErrorId() {
        return errorId;
    }
    
    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getSeverity() {
        return severity;
    }
    
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public ExceptionContext getContext() {
        return context;
    }
    
    public void setContext(ExceptionContext context) {
        this.context = context;
    }
    
    public String[] getStackTrace() {
        return stackTrace;
    }
    
    public void setStackTrace(String[] stackTrace) {
        this.stackTrace = stackTrace;
    }
    
    /**
     * Builder class for ExceptionDetails.
     */
    public static class Builder {
        private String errorId;
        private String errorCode;
        private String message;
        private String category;
        private String severity;
        private LocalDateTime timestamp;
        private ExceptionContext context;
        private String[] stackTrace;
        
        public Builder errorId(String errorId) {
            this.errorId = errorId;
            return this;
        }
        
        public Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }
        
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        
        public Builder category(String category) {
            this.category = category;
            return this;
        }
        
        public Builder severity(String severity) {
            this.severity = severity;
            return this;
        }
        
        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder context(ExceptionContext context) {
            this.context = context;
            return this;
        }
        
        public Builder stackTrace(String[] stackTrace) {
            this.stackTrace = stackTrace;
            return this;
        }
        
        public ExceptionDetails build() {
            return new ExceptionDetails(errorId, errorCode, message, category, severity, 
                                      timestamp, context, stackTrace);
        }
    }
}
