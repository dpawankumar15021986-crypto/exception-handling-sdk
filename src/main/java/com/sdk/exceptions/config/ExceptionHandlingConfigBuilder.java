package com.sdk.exceptions.config;

/**
 * Fluent builder for ExceptionHandlingConfig with method chaining.
 * Provides a convenient way to create configurations with various presets.
 */
public class ExceptionHandlingConfigBuilder {
    
    private final ExceptionHandlingConfig.Builder builder;
    
    /**
     * Creates a new config builder.
     */
    public ExceptionHandlingConfigBuilder() {
        this.builder = ExceptionHandlingConfig.builder();
    }
    
    /**
     * Creates a new config builder starting from an existing configuration.
     *
     * @param baseConfig the base configuration to copy from
     */
    public ExceptionHandlingConfigBuilder(ExceptionHandlingConfig baseConfig) {
        this.builder = ExceptionHandlingConfig.builder()
                .includeStackTrace(baseConfig.isIncludeStackTrace())
                .includeExceptionDetails(baseConfig.isIncludeExceptionDetails())
                .logExceptions(baseConfig.isLogExceptions())
                .logLevel(baseConfig.getLogLevel())
                .sanitizeMessages(baseConfig.isSanitizeMessages())
                .maxStackTraceElements(baseConfig.getMaxStackTraceElements())
                .enableCaching(baseConfig.isEnableCaching())
                .dateTimeFormat(baseConfig.getDateTimeFormat());
    }
    
    /**
     * Starts with a development configuration.
     *
     * @return a new builder with development settings
     */
    public static ExceptionHandlingConfigBuilder forDevelopment() {
        return new ExceptionHandlingConfigBuilder()
                .withStackTraces()
                .withDetailedExceptions()
                .withLogging()
                .withLogLevel("DEBUG")
                .withoutMessageSanitization()
                .withUnlimitedStackTrace();
    }
    
    /**
     * Starts with a production configuration.
     *
     * @return a new builder with production settings
     */
    public static ExceptionHandlingConfigBuilder forProduction() {
        return new ExceptionHandlingConfigBuilder()
                .withoutStackTraces()
                .withoutDetailedExceptions()
                .withLogging()
                .withLogLevel("ERROR")
                .withMessageSanitization()
                .withLimitedStackTrace(10);
    }
    
    /**
     * Starts with a testing configuration.
     *
     * @return a new builder with testing settings
     */
    public static ExceptionHandlingConfigBuilder forTesting() {
        return new ExceptionHandlingConfigBuilder()
                .withStackTraces()
                .withDetailedExceptions()
                .withLogging()
                .withLogLevel("WARN")
                .withoutMessageSanitization()
                .withCaching(false);
    }
    
    /**
     * Enables stack traces in error responses.
     *
     * @return this builder
     */
    public ExceptionHandlingConfigBuilder withStackTraces() {
        builder.includeStackTrace(true);
        return this;
    }
    
    /**
     * Disables stack traces in error responses.
     *
     * @return this builder
     */
    public ExceptionHandlingConfigBuilder withoutStackTraces() {
        builder.includeStackTrace(false);
        return this;
    }
    
    /**
     * Enables detailed exception information in error responses.
     *
     * @return this builder
     */
    public ExceptionHandlingConfigBuilder withDetailedExceptions() {
        builder.includeExceptionDetails(true);
        return this;
    }
    
    /**
     * Disables detailed exception information in error responses.
     *
     * @return this builder
     */
    public ExceptionHandlingConfigBuilder withoutDetailedExceptions() {
        builder.includeExceptionDetails(false);
        return this;
    }
    
    /**
     * Enables exception logging.
     *
     * @return this builder
     */
    public ExceptionHandlingConfigBuilder withLogging() {
        builder.logExceptions(true);
        return this;
    }
    
    /**
     * Disables exception logging.
     *
     * @return this builder
     */
    public ExceptionHandlingConfigBuilder withoutLogging() {
        builder.logExceptions(false);
        return this;
    }
    
    /**
     * Sets the log level for exception logging.
     *
     * @param level the log level (TRACE, DEBUG, INFO, WARN, ERROR)
     * @return this builder
     */
    public ExceptionHandlingConfigBuilder withLogLevel(String level) {
        builder.logLevel(level);
        return this;
    }
    
    /**
     * Enables message sanitization for security.
     *
     * @return this builder
     */
    public ExceptionHandlingConfigBuilder withMessageSanitization() {
        builder.sanitizeMessages(true);
        return this;
    }
    
    /**
     * Disables message sanitization.
     *
     * @return this builder
     */
    public ExceptionHandlingConfigBuilder withoutMessageSanitization() {
        builder.sanitizeMessages(false);
        return this;
    }
    
    /**
     * Sets a limit on the number of stack trace elements.
     *
     * @param maxElements the maximum number of stack trace elements
     * @return this builder
     */
    public ExceptionHandlingConfigBuilder withLimitedStackTrace(int maxElements) {
        builder.maxStackTraceElements(maxElements);
        return this;
    }
    
    /**
     * Removes limits on stack trace elements.
     *
     * @return this builder
     */
    public ExceptionHandlingConfigBuilder withUnlimitedStackTrace() {
        builder.maxStackTraceElements(Integer.MAX_VALUE);
        return this;
    }
    
    /**
     * Sets whether to enable handler caching.
     *
     * @param enabled true to enable caching
     * @return this builder
     */
    public ExceptionHandlingConfigBuilder withCaching(boolean enabled) {
        builder.enableCaching(enabled);
        return this;
    }
    
    /**
     * Sets the date-time format for timestamps.
     *
     * @param format the date-time format pattern
     * @return this builder
     */
    public ExceptionHandlingConfigBuilder withDateTimeFormat(String format) {
        builder.dateTimeFormat(format);
        return this;
    }
    
    /**
     * Configures for high-security environments.
     * Minimal error information exposure.
     *
     * @return this builder
     */
    public ExceptionHandlingConfigBuilder forHighSecurity() {
        return withoutStackTraces()
                .withoutDetailedExceptions()
                .withMessageSanitization()
                .withLogging()
                .withLogLevel("ERROR");
    }
    
    /**
     * Configures for debugging scenarios.
     * Maximum error information for troubleshooting.
     *
     * @return this builder
     */
    public ExceptionHandlingConfigBuilder forDebugging() {
        return withStackTraces()
                .withDetailedExceptions()
                .withoutMessageSanitization()
                .withLogging()
                .withLogLevel("DEBUG")
                .withUnlimitedStackTrace();
    }
    
    /**
     * Builds the final configuration.
     *
     * @return the exception handling configuration
     */
    public ExceptionHandlingConfig build() {
        return builder.build();
    }
}
