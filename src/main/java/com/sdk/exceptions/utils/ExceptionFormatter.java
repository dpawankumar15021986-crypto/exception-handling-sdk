package com.sdk.exceptions.utils;

import com.sdk.exceptions.core.ExceptionContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class for formatting exceptions into various output formats.
 * Provides methods to convert exceptions to JSON, XML, plain text, and structured formats.
 */
public class ExceptionFormatter {
    
    private static final Logger logger = LoggerFactory.getLogger(ExceptionFormatter.class);
    
    private final ObjectMapper objectMapper;
    private final DateTimeFormatter dateTimeFormatter;
    
    /**
     * Constructs an ExceptionFormatter with default settings.
     */
    public ExceptionFormatter() {
        this(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
    
    /**
     * Constructs an ExceptionFormatter with a custom date-time formatter.
     *
     * @param dateTimeFormatter the date-time formatter to use
     */
    public ExceptionFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
        this.objectMapper = createObjectMapper();
    }
    
    /**
     * Creates and configures the ObjectMapper for JSON serialization.
     *
     * @return the configured ObjectMapper
     */
    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }
    
    /**
     * Formats an exception as JSON.
     *
     * @param exception the exception to format
     * @return the JSON representation
     */
    public String toJson(Throwable exception) {
        return toJson(exception, true);
    }
    
    /**
     * Formats an exception as JSON with optional stack trace.
     *
     * @param exception the exception to format
     * @param includeStackTrace whether to include the stack trace
     * @return the JSON representation
     */
    public String toJson(Throwable exception, boolean includeStackTrace) {
        try {
            Map<String, Object> exceptionData = createExceptionMap(exception, includeStackTrace);
            return objectMapper.writeValueAsString(exceptionData);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize exception to JSON", e);
            return createFallbackJson(exception);
        }
    }
    
    /**
     * Formats an exception as a structured map.
     *
     * @param exception the exception to format
     * @param includeStackTrace whether to include the stack trace
     * @return the structured map
     */
    public Map<String, Object> toMap(Throwable exception, boolean includeStackTrace) {
        return createExceptionMap(exception, includeStackTrace);
    }
    
    /**
     * Formats an exception as plain text.
     *
     * @param exception the exception to format
     * @return the plain text representation
     */
    public String toPlainText(Throwable exception) {
        return toPlainText(exception, true);
    }
    
    /**
     * Formats an exception as plain text with optional stack trace.
     *
     * @param exception the exception to format
     * @param includeStackTrace whether to include the stack trace
     * @return the plain text representation
     */
    public String toPlainText(Throwable exception, boolean includeStackTrace) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Exception: ").append(exception.getClass().getSimpleName()).append("\n");
        sb.append("Message: ").append(exception.getMessage()).append("\n");
        sb.append("Timestamp: ").append(LocalDateTime.now().format(dateTimeFormatter)).append("\n");
        
        // Add SDK-specific information if available
        addSdkInfoToText(exception, sb);
        
        // Add cause information
        Throwable cause = exception.getCause();
        if (cause != null) {
            sb.append("Caused by: ").append(cause.getClass().getSimpleName())
              .append(": ").append(cause.getMessage()).append("\n");
        }
        
        // Add stack trace if requested
        if (includeStackTrace) {
            sb.append("\nStack Trace:\n");
            sb.append(getStackTraceString(exception));
        }
        
        return sb.toString();
    }
    
    /**
     * Formats an exception as XML.
     *
     * @param exception the exception to format
     * @return the XML representation
     */
    public String toXml(Throwable exception) {
        return toXml(exception, true);
    }
    
    /**
     * Formats an exception as XML with optional stack trace.
     *
     * @param exception the exception to format
     * @param includeStackTrace whether to include the stack trace
     * @return the XML representation
     */
    public String toXml(Throwable exception, boolean includeStackTrace) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<exception>\n");
        
        sb.append("  <class>").append(escapeXml(exception.getClass().getSimpleName())).append("</class>\n");
        sb.append("  <message>").append(escapeXml(exception.getMessage())).append("</message>\n");
        sb.append("  <timestamp>").append(LocalDateTime.now().format(dateTimeFormatter)).append("</timestamp>\n");
        
        // Add SDK-specific information if available
        addSdkInfoToXml(exception, sb);
        
        // Add cause information
        Throwable cause = exception.getCause();
        if (cause != null) {
            sb.append("  <cause>\n");
            sb.append("    <class>").append(escapeXml(cause.getClass().getSimpleName())).append("</class>\n");
            sb.append("    <message>").append(escapeXml(cause.getMessage())).append("</message>\n");
            sb.append("  </cause>\n");
        }
        
        // Add stack trace if requested
        if (includeStackTrace) {
            sb.append("  <stackTrace>\n");
            for (StackTraceElement element : exception.getStackTrace()) {
                sb.append("    <frame>").append(escapeXml(element.toString())).append("</frame>\n");
            }
            sb.append("  </stackTrace>\n");
        }
        
        sb.append("</exception>");
        return sb.toString();
    }
    
    /**
     * Formats an exception for logging purposes.
     *
     * @param exception the exception to format
     * @return the log-friendly representation
     */
    public String toLogFormat(Throwable exception) {
        StringBuilder sb = new StringBuilder();
        
        // Basic exception info
        sb.append("[").append(exception.getClass().getSimpleName()).append("] ");
        sb.append(exception.getMessage());
        
        // Add SDK-specific information if available
        String errorId = ExceptionUtils.getErrorId(exception);
        if (errorId != null) {
            sb.append(" [errorId: ").append(errorId).append("]");
        }
        
        String errorCode = ExceptionUtils.getErrorCode(exception);
        if (errorCode != null) {
            sb.append(" [errorCode: ").append(errorCode).append("]");
        }
        
        // Add cause if present
        Throwable cause = exception.getCause();
        if (cause != null) {
            sb.append(" caused by ").append(cause.getClass().getSimpleName())
              .append(": ").append(cause.getMessage());
        }
        
        return sb.toString();
    }
    
    /**
     * Creates a structured map representation of an exception.
     *
     * @param exception the exception
     * @param includeStackTrace whether to include stack trace
     * @return the exception map
     */
    private Map<String, Object> createExceptionMap(Throwable exception, boolean includeStackTrace) {
        Map<String, Object> exceptionMap = new LinkedHashMap<>();
        
        exceptionMap.put("class", exception.getClass().getSimpleName());
        exceptionMap.put("message", exception.getMessage());
        exceptionMap.put("timestamp", LocalDateTime.now().format(dateTimeFormatter));
        
        // Add SDK-specific information if available
        addSdkInfoToMap(exception, exceptionMap);
        
        // Add cause information
        Throwable cause = exception.getCause();
        if (cause != null) {
            Map<String, Object> causeMap = new LinkedHashMap<>();
            causeMap.put("class", cause.getClass().getSimpleName());
            causeMap.put("message", cause.getMessage());
            exceptionMap.put("cause", causeMap);
        }
        
        // Add stack trace if requested
        if (includeStackTrace) {
            String[] stackTrace = new String[exception.getStackTrace().length];
            for (int i = 0; i < exception.getStackTrace().length; i++) {
                stackTrace[i] = exception.getStackTrace()[i].toString();
            }
            exceptionMap.put("stackTrace", stackTrace);
        }
        
        return exceptionMap;
    }
    
    /**
     * Adds SDK-specific information to a map.
     *
     * @param exception the exception
     * @param map the map to add information to
     */
    private void addSdkInfoToMap(Throwable exception, Map<String, Object> map) {
        String errorId = ExceptionUtils.getErrorId(exception);
        if (errorId != null) {
            map.put("errorId", errorId);
        }
        
        String errorCode = ExceptionUtils.getErrorCode(exception);
        if (errorCode != null) {
            map.put("errorCode", errorCode);
        }
        
        String category = ExceptionUtils.getCategory(exception);
        if (category != null) {
            map.put("category", category);
        }
        
        String severity = ExceptionUtils.getSeverity(exception);
        if (severity != null) {
            map.put("severity", severity);
        }
        
        ExceptionContext context = ExceptionUtils.getContext(exception);
        if (context != null) {
            Map<String, Object> contextData = context.getAllContextData();
            if (!contextData.isEmpty()) {
                map.put("contextData", contextData);
            }
            
            Map<String, String> metadata = context.getAllMetadata();
            if (!metadata.isEmpty()) {
                map.put("metadata", metadata);
            }
        }
    }
    
    /**
     * Adds SDK-specific information to plain text.
     *
     * @param exception the exception
     * @param sb the StringBuilder to append to
     */
    private void addSdkInfoToText(Throwable exception, StringBuilder sb) {
        String errorId = ExceptionUtils.getErrorId(exception);
        if (errorId != null) {
            sb.append("Error ID: ").append(errorId).append("\n");
        }
        
        String errorCode = ExceptionUtils.getErrorCode(exception);
        if (errorCode != null) {
            sb.append("Error Code: ").append(errorCode).append("\n");
        }
        
        String category = ExceptionUtils.getCategory(exception);
        if (category != null) {
            sb.append("Category: ").append(category).append("\n");
        }
        
        String severity = ExceptionUtils.getSeverity(exception);
        if (severity != null) {
            sb.append("Severity: ").append(severity).append("\n");
        }
    }
    
    /**
     * Adds SDK-specific information to XML.
     *
     * @param exception the exception
     * @param sb the StringBuilder to append to
     */
    private void addSdkInfoToXml(Throwable exception, StringBuilder sb) {
        String errorId = ExceptionUtils.getErrorId(exception);
        if (errorId != null) {
            sb.append("  <errorId>").append(escapeXml(errorId)).append("</errorId>\n");
        }
        
        String errorCode = ExceptionUtils.getErrorCode(exception);
        if (errorCode != null) {
            sb.append("  <errorCode>").append(escapeXml(errorCode)).append("</errorCode>\n");
        }
        
        String category = ExceptionUtils.getCategory(exception);
        if (category != null) {
            sb.append("  <category>").append(escapeXml(category)).append("</category>\n");
        }
        
        String severity = ExceptionUtils.getSeverity(exception);
        if (severity != null) {
            sb.append("  <severity>").append(escapeXml(severity)).append("</severity>\n");
        }
    }
    
    /**
     * Gets the stack trace as a string.
     *
     * @param exception the exception
     * @return the stack trace string
     */
    private String getStackTraceString(Throwable exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw.toString();
    }
    
    /**
     * Creates a fallback JSON representation when serialization fails.
     *
     * @param exception the exception
     * @return the fallback JSON
     */
    private String createFallbackJson(Throwable exception) {
        return String.format(
                "{\"class\":\"%s\",\"message\":\"%s\",\"timestamp\":\"%s\",\"error\":\"Failed to serialize exception\"}",
                exception.getClass().getSimpleName(),
                escapeJson(exception.getMessage()),
                LocalDateTime.now().format(dateTimeFormatter)
        );
    }
    
    /**
     * Escapes XML special characters.
     *
     * @param text the text to escape
     * @return the escaped text
     */
    private String escapeXml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }
    
    /**
     * Escapes JSON special characters.
     *
     * @param text the text to escape
     * @return the escaped text
     */
    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}
