package com.sdk.exceptions.utils;

import com.sdk.exceptions.core.ExceptionContext;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Utility class providing helper methods for working with exceptions.
 * Includes reflection-based access to SDK exception properties and general exception utilities.
 */
public class ExceptionUtils {
    
    /**
     * Private constructor to prevent instantiation.
     */
    private ExceptionUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Gets the error ID from an exception if available.
     *
     * @param exception the exception
     * @return the error ID, or null if not available
     */
    public static String getErrorId(Throwable exception) {
        return invokeMethod(exception, "getErrorId", String.class);
    }
    
    /**
     * Gets the error code from an exception if available.
     *
     * @param exception the exception
     * @return the error code, or null if not available
     */
    public static String getErrorCode(Throwable exception) {
        return invokeMethod(exception, "getErrorCode", String.class);
    }
    
    /**
     * Gets the category from an exception if available.
     *
     * @param exception the exception
     * @return the category, or null if not available
     */
    public static String getCategory(Throwable exception) {
        return invokeMethod(exception, "getCategory", String.class);
    }
    
    /**
     * Gets the severity from an exception if available.
     *
     * @param exception the exception
     * @return the severity, or null if not available
     */
    public static String getSeverity(Throwable exception) {
        return invokeMethod(exception, "getSeverity", String.class);
    }
    
    /**
     * Gets the timestamp from an exception if available.
     *
     * @param exception the exception
     * @return the timestamp, or null if not available
     */
    public static java.time.LocalDateTime getTimestamp(Throwable exception) {
        return invokeMethod(exception, "getTimestamp", java.time.LocalDateTime.class);
    }
    
    /**
     * Gets the context from an exception if available.
     *
     * @param exception the exception
     * @return the context, or null if not available
     */
    public static ExceptionContext getContext(Throwable exception) {
        return invokeMethod(exception, "getContext", ExceptionContext.class);
    }
    
    /**
     * Gets the HTTP status code from an HTTP exception if available.
     *
     * @param exception the exception
     * @return the HTTP status code, or null if not available
     */
    public static Integer getHttpStatusCode(Throwable exception) {
        return invokeMethod(exception, "getStatusCodeValue", Integer.class);
    }
    
    /**
     * Checks if an exception is a checked exception (extends Exception but not RuntimeException).
     *
     * @param exception the exception
     * @return true if it's a checked exception, false otherwise
     */
    public static boolean isCheckedException(Throwable exception) {
        return exception instanceof Exception && !(exception instanceof RuntimeException);
    }
    
    /**
     * Checks if an exception is an unchecked exception (extends RuntimeException).
     *
     * @param exception the exception
     * @return true if it's an unchecked exception, false otherwise
     */
    public static boolean isUncheckedException(Throwable exception) {
        return exception instanceof RuntimeException;
    }
    
    /**
     * Checks if an exception is a system error (extends Error).
     *
     * @param exception the exception
     * @return true if it's a system error, false otherwise
     */
    public static boolean isSystemError(Throwable exception) {
        return exception instanceof Error;
    }
    
    /**
     * Gets the root cause of an exception.
     *
     * @param exception the exception
     * @return the root cause
     */
    public static Throwable getRootCause(Throwable exception) {
        Throwable cause = exception;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }
    
    /**
     * Gets the complete chain of causes for an exception.
     *
     * @param exception the exception
     * @return the list of causes from the exception to the root cause
     */
    public static List<Throwable> getCauseChain(Throwable exception) {
        List<Throwable> chain = new ArrayList<>();
        Throwable current = exception;
        
        while (current != null) {
            chain.add(current);
            current = current.getCause();
            
            // Prevent infinite loops
            if (chain.contains(current)) {
                break;
            }
        }
        
        return Collections.unmodifiableList(chain);
    }
    
    /**
     * Gets the stack trace of an exception as a string.
     *
     * @param exception the exception
     * @return the stack trace as a string
     */
    public static String getStackTraceAsString(Throwable exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw.toString();
    }
    
    /**
     * Gets the stack trace of an exception as an array of strings.
     *
     * @param exception the exception
     * @return the stack trace as a string array
     */
    public static String[] getStackTraceAsArray(Throwable exception) {
        StackTraceElement[] elements = exception.getStackTrace();
        String[] stackTrace = new String[elements.length];
        for (int i = 0; i < elements.length; i++) {
            stackTrace[i] = elements[i].toString();
        }
        return stackTrace;
    }
    
    /**
     * Gets a limited stack trace with a maximum number of elements.
     *
     * @param exception the exception
     * @param maxElements the maximum number of elements to include
     * @return the limited stack trace
     */
    public static String[] getLimitedStackTrace(Throwable exception, int maxElements) {
        StackTraceElement[] elements = exception.getStackTrace();
        int length = Math.min(elements.length, maxElements);
        String[] stackTrace = new String[length];
        for (int i = 0; i < length; i++) {
            stackTrace[i] = elements[i].toString();
        }
        return stackTrace;
    }
    
    /**
     * Checks if an exception has a cause.
     *
     * @param exception the exception
     * @return true if the exception has a cause, false otherwise
     */
    public static boolean hasCause(Throwable exception) {
        return exception.getCause() != null;
    }
    
    /**
     * Checks if an exception is caused by a specific exception type.
     *
     * @param exception the exception
     * @param causeType the expected cause type
     * @return true if the exception is caused by the specified type, false otherwise
     */
    public static boolean isCausedBy(Throwable exception, Class<? extends Throwable> causeType) {
        return findCause(exception, causeType).isPresent();
    }
    
    /**
     * Finds a cause of a specific type in the exception chain.
     *
     * @param exception the exception
     * @param causeType the cause type to find
     * @param <T> the cause type
     * @return the cause if found, empty otherwise
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> Optional<T> findCause(Throwable exception, Class<T> causeType) {
        Throwable current = exception;
        while (current != null) {
            if (causeType.isInstance(current)) {
                return Optional.of((T) current);
            }
            current = current.getCause();
        }
        return Optional.empty();
    }
    
    /**
     * Sanitizes an exception message by removing potentially sensitive information.
     *
     * @param message the original message
     * @return the sanitized message
     */
    public static String sanitizeMessage(String message) {
        if (message == null) {
            return null;
        }
        
        // Remove common patterns that might contain sensitive data
        String sanitized = message
                // Remove file paths
                .replaceAll("[A-Za-z]:\\\\[^\\s]+", "[REDACTED_PATH]")
                .replaceAll("/[^\\s]+/[^\\s]+", "[REDACTED_PATH]")
                // Remove URLs
                .replaceAll("https?://[^\\s]+", "[REDACTED_URL]")
                // Remove email addresses
                .replaceAll("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b", "[REDACTED_EMAIL]")
                // Remove potential passwords or tokens
                .replaceAll("(?i)(password|token|secret|key)\\s*[:=]\\s*[^\\s]+", "$1=[REDACTED]");
        
        return sanitized;
    }
    
    /**
     * Creates a simplified exception message suitable for end users.
     *
     * @param exception the exception
     * @return the simplified message
     */
    public static String getSimplifiedMessage(Throwable exception) {
        String className = exception.getClass().getSimpleName();
        String message = exception.getMessage();
        
        // Map common exception types to user-friendly messages
        switch (className) {
            case "NullPointerException":
                return "A required value was not provided";
            case "IllegalArgumentException":
                return "An invalid parameter was provided";
            case "IOException":
                return "A file or network operation failed";
            case "SQLException":
                return "A database operation failed";
            case "TimeoutException":
                return "The operation timed out";
            case "OutOfMemoryError":
                return "The system is out of memory";
            default:
                return message != null ? message : "An error occurred";
        }
    }
    
    /**
     * Invokes a method on an object using reflection.
     *
     * @param obj the object
     * @param methodName the method name
     * @param returnType the expected return type
     * @param <T> the return type
     * @return the method result, or null if not available
     */
    @SuppressWarnings("unchecked")
    private static <T> T invokeMethod(Object obj, String methodName, Class<T> returnType) {
        try {
            Method method = obj.getClass().getMethod(methodName);
            Object result = method.invoke(obj);
            if (result != null && returnType.isInstance(result)) {
                return (T) result;
            }
        } catch (Exception e) {
            // Ignore reflection errors - method might not exist
        }
        return null;
    }
}
