package com.sdk.exceptions.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sdk.exceptions.core.ExceptionContext;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Detailed exception information model.
 * Contains comprehensive information about an exception for debugging and analysis.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionDetails {
    
    @JsonProperty("errorId")
    private final String errorId;
    
    @JsonProperty("errorCode")
    private final String errorCode;
    
    @JsonProperty("message")
    private final String message;
    
    @JsonProperty("category")
    private final String category;
    
    @JsonProperty("severity")
    private final String severity;
    
    @JsonProperty("timestamp")
    private final LocalDateTime timestamp;
    
    @JsonProperty("context")
    private final ExceptionContext context;
    
    @JsonProperty("stackTrace")
    private final String[] stackTrace;
    
    @JsonProperty("causeDetails")
    private final ExceptionDetails causeDetails;
    
    /**
     * Private constructor for builder pattern.
     */
    private ExceptionDetails(Builder builder) {
        this.errorId = builder.errorId;
        this.errorCode = builder.errorCode;
        this.message = builder.message;
        this.category = builder.category;
        this.severity = builder.severity;
        this.timestamp = builder.timestamp;
        this.context = builder.context;
        this.stackTrace = builder.stackTrace;
        this.causeDetails = builder.causeDetails;
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
     * Creates simple exception details with just a message.
     *
     * @param message the error message
     * @return the exception details
     */
    public static ExceptionDetails simple(String message) {
        return builder()
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates exception details with error code and message.
     *
     * @param errorCode the error code
     * @param message the error message
     * @return the exception details
     */
    public static ExceptionDetails withCode(String errorCode, String message) {
        return builder()
                .errorCode(errorCode)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Gets the error ID.
     *
     * @return the error ID
     */
    public String getErrorId() {
        return errorId;
    }
    
    /**
     * Gets the error code.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
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
     * Gets the exception category.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * Gets the severity level.
     *
     * @return the severity level
     */
    public String getSeverity() {
        return severity;
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
     * Gets the exception context.
     *
     * @return the exception context
     */
    public ExceptionContext getContext() {
        return context;
    }
    
    /**
     * Gets the stack trace.
     *
     * @return the stack trace as string array
     */
    public String[] getStackTrace() {
        return stackTrace != null ? stackTrace.clone() : null;
    }
    
    /**
     * Gets the cause details.
     *
     * @return the cause details
     */
    public ExceptionDetails getCauseDetails() {
        return causeDetails;
    }
    
    /**
     * Checks if this has an error ID.
     *
     * @return true if error ID is present, false otherwise
     */
    public boolean hasErrorId() {
        return errorId != null && !errorId.isEmpty();
    }
    
    /**
     * Checks if this has an error code.
     *
     * @return true if error code is present, false otherwise
     */
    public boolean hasErrorCode() {
        return errorCode != null && !errorCode.isEmpty();
    }
    
    /**
     * Checks if this has context information.
     *
     * @return true if context is present, false otherwise
     */
    public boolean hasContext() {
        return context != null;
    }
    
    /**
     * Checks if this has a stack trace.
     *
     * @return true if stack trace is present, false otherwise
     */
    public boolean hasStackTrace() {
        return stackTrace != null && stackTrace.length > 0;
    }
    
    /**
     * Checks if this has cause details.
     *
     * @return true if cause details are present, false otherwise
     */
    public boolean hasCauseDetails() {
        return causeDetails != null;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExceptionDetails that = (ExceptionDetails) o;
        return Objects.equals(errorId, that.errorId) &&
                Objects.equals(errorCode, that.errorCode) &&
                Objects.equals(message, that.message) &&
                Objects.equals(category, that.category) &&
                Objects.equals(severity, that.severity) &&
                Objects.equals(timestamp, that.timestamp);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(errorId, errorCode, message, category, severity, timestamp);
    }
    
    @Override
    public String toString() {
        return String.format("ExceptionDetails{errorCode='%s', errorId='%s', category='%s', severity='%s', message='%s'}",
                errorCode, errorId, category, severity, message);
    }
    
    /**
     * Builder for ExceptionDetails.
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
        private ExceptionDetails causeDetails;
        
        /**
         * Sets the error ID.
         *
         * @param errorId the error ID
         * @return this builder
         */
        public Builder errorId(String errorId) {
            this.errorId = errorId;
            return this;
        }
        
        /**
         * Sets the error code.
         *
         * @param errorCode the error code
         * @return this builder
         */
        public Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
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
         * Sets the exception category.
         *
         * @param category the category
         * @return this builder
         */
        public Builder category(String category) {
            this.category = category;
            return this;
        }
        
        /**
         * Sets the severity level.
         *
         * @param severity the severity level
         * @return this builder
         */
        public Builder severity(String severity) {
            this.severity = severity;
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
         * Sets the exception context.
         *
         * @param context the exception context
         * @return this builder
         */
        public Builder context(ExceptionContext context) {
            this.context = context;
            return this;
        }
        
        /**
         * Sets the stack trace.
         *
         * @param stackTrace the stack trace
         * @return this builder
         */
        public Builder stackTrace(String[] stackTrace) {
            this.stackTrace = stackTrace != null ? stackTrace.clone() : null;
            return this;
        }
        
        /**
         * Sets the cause details.
         *
         * @param causeDetails the cause details
         * @return this builder
         */
        public Builder causeDetails(ExceptionDetails causeDetails) {
            this.causeDetails = causeDetails;
            return this;
        }
        
        /**
         * Builds the exception details.
         *
         * @return the exception details
         */
        public ExceptionDetails build() {
            return new ExceptionDetails(this);
        }
    }
}
