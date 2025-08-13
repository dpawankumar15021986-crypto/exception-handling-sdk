package com.sdk.exceptions.config;

import java.util.Objects;

/**
 * Configuration for exception handling behavior.
 * Provides options to customize how exceptions are processed and logged.
 */
public class ExceptionHandlingConfig {
    
    private final boolean includeStackTrace;
    private final boolean includeExceptionDetails;
    private final boolean logExceptions;
    private final String logLevel;
    private final boolean sanitizeMessages;
    private final int maxStackTraceElements;
    private final boolean enableCaching;
    private final String dateTimeFormat;
    
    /**
     * Private constructor for builder pattern.
     */
    private ExceptionHandlingConfig(Builder builder) {
        this.includeStackTrace = builder.includeStackTrace;
        this.includeExceptionDetails = builder.includeExceptionDetails;
        this.logExceptions = builder.logExceptions;
        this.logLevel = builder.logLevel;
        this.sanitizeMessages = builder.sanitizeMessages;
        this.maxStackTraceElements = builder.maxStackTraceElements;
        this.enableCaching = builder.enableCaching;
        this.dateTimeFormat = builder.dateTimeFormat;
    }
    
    /**
     * Creates a default configuration.
     *
     * @return the default configuration
     */
    public static ExceptionHandlingConfig defaultConfig() {
        return builder().build();
    }
    
    /**
     * Creates a development configuration with detailed error information.
     *
     * @return the development configuration
     */
    public static ExceptionHandlingConfig developmentConfig() {
        return builder()
                .includeStackTrace(true)
                .includeExceptionDetails(true)
                .logExceptions(true)
                .sanitizeMessages(false)
                .logLevel("DEBUG")
                .build();
    }
    
    /**
     * Creates a production configuration with minimal error exposure.
     *
     * @return the production configuration
     */
    public static ExceptionHandlingConfig productionConfig() {
        return builder()
                .includeStackTrace(false)
                .includeExceptionDetails(false)
                .logExceptions(true)
                .sanitizeMessages(true)
                .logLevel("ERROR")
                .maxStackTraceElements(10)
                .build();
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
     * Checks if stack traces should be included in error responses.
     *
     * @return true if stack traces should be included, false otherwise
     */
    public boolean isIncludeStackTrace() {
        return includeStackTrace;
    }
    
    /**
     * Checks if exception details should be included in error responses.
     *
     * @return true if exception details should be included, false otherwise
     */
    public boolean isIncludeExceptionDetails() {
        return includeExceptionDetails;
    }
    
    /**
     * Checks if exceptions should be logged.
     *
     * @return true if exceptions should be logged, false otherwise
     */
    public boolean isLogExceptions() {
        return logExceptions;
    }
    
    /**
     * Gets the log level for exception logging.
     *
     * @return the log level
     */
    public String getLogLevel() {
        return logLevel;
    }
    
    /**
     * Checks if error messages should be sanitized.
     *
     * @return true if messages should be sanitized, false otherwise
     */
    public boolean isSanitizeMessages() {
        return sanitizeMessages;
    }
    
    /**
     * Gets the maximum number of stack trace elements to include.
     *
     * @return the maximum stack trace elements
     */
    public int getMaxStackTraceElements() {
        return maxStackTraceElements;
    }
    
    /**
     * Checks if handler caching is enabled.
     *
     * @return true if caching is enabled, false otherwise
     */
    public boolean isEnableCaching() {
        return enableCaching;
    }
    
    /**
     * Gets the date-time format for timestamps.
     *
     * @return the date-time format
     */
    public String getDateTimeFormat() {
        return dateTimeFormat;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExceptionHandlingConfig that = (ExceptionHandlingConfig) o;
        return includeStackTrace == that.includeStackTrace &&
                includeExceptionDetails == that.includeExceptionDetails &&
                logExceptions == that.logExceptions &&
                sanitizeMessages == that.sanitizeMessages &&
                maxStackTraceElements == that.maxStackTraceElements &&
                enableCaching == that.enableCaching &&
                Objects.equals(logLevel, that.logLevel) &&
                Objects.equals(dateTimeFormat, that.dateTimeFormat);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(includeStackTrace, includeExceptionDetails, logExceptions, 
                logLevel, sanitizeMessages, maxStackTraceElements, enableCaching, dateTimeFormat);
    }
    
    @Override
    public String toString() {
        return String.format("ExceptionHandlingConfig{" +
                        "includeStackTrace=%s, includeExceptionDetails=%s, logExceptions=%s, " +
                        "logLevel='%s', sanitizeMessages=%s, maxStackTraceElements=%d, " +
                        "enableCaching=%s, dateTimeFormat='%s'}",
                includeStackTrace, includeExceptionDetails, logExceptions,
                logLevel, sanitizeMessages, maxStackTraceElements,
                enableCaching, dateTimeFormat);
    }
    
    /**
     * Builder for ExceptionHandlingConfig.
     */
    public static class Builder {
        private boolean includeStackTrace = false;
        private boolean includeExceptionDetails = true;
        private boolean logExceptions = true;
        private String logLevel = "ERROR";
        private boolean sanitizeMessages = true;
        private int maxStackTraceElements = 20;
        private boolean enableCaching = true;
        private String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        
        /**
         * Sets whether to include stack traces in error responses.
         *
         * @param includeStackTrace true to include stack traces
         * @return this builder
         */
        public Builder includeStackTrace(boolean includeStackTrace) {
            this.includeStackTrace = includeStackTrace;
            return this;
        }
        
        /**
         * Sets whether to include exception details in error responses.
         *
         * @param includeExceptionDetails true to include exception details
         * @return this builder
         */
        public Builder includeExceptionDetails(boolean includeExceptionDetails) {
            this.includeExceptionDetails = includeExceptionDetails;
            return this;
        }
        
        /**
         * Sets whether to log exceptions.
         *
         * @param logExceptions true to log exceptions
         * @return this builder
         */
        public Builder logExceptions(boolean logExceptions) {
            this.logExceptions = logExceptions;
            return this;
        }
        
        /**
         * Sets the log level for exception logging.
         *
         * @param logLevel the log level
         * @return this builder
         */
        public Builder logLevel(String logLevel) {
            this.logLevel = logLevel;
            return this;
        }
        
        /**
         * Sets whether to sanitize error messages.
         *
         * @param sanitizeMessages true to sanitize messages
         * @return this builder
         */
        public Builder sanitizeMessages(boolean sanitizeMessages) {
            this.sanitizeMessages = sanitizeMessages;
            return this;
        }
        
        /**
         * Sets the maximum number of stack trace elements to include.
         *
         * @param maxStackTraceElements the maximum stack trace elements
         * @return this builder
         */
        public Builder maxStackTraceElements(int maxStackTraceElements) {
            this.maxStackTraceElements = Math.max(0, maxStackTraceElements);
            return this;
        }
        
        /**
         * Sets whether to enable handler caching.
         *
         * @param enableCaching true to enable caching
         * @return this builder
         */
        public Builder enableCaching(boolean enableCaching) {
            this.enableCaching = enableCaching;
            return this;
        }
        
        /**
         * Sets the date-time format for timestamps.
         *
         * @param dateTimeFormat the date-time format
         * @return this builder
         */
        public Builder dateTimeFormat(String dateTimeFormat) {
            this.dateTimeFormat = dateTimeFormat;
            return this;
        }
        
        /**
         * Builds the configuration.
         *
         * @return the configuration
         */
        public ExceptionHandlingConfig build() {
            return new ExceptionHandlingConfig(this);
        }
    }
}
