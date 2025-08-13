package com.sdk.exceptions.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SystemErrorException.
 */
class SystemErrorExceptionTest {
    
    @Test
    void testConstructorWithError() {
        OutOfMemoryError error = new OutOfMemoryError("Java heap space");
        SystemErrorException exception = new SystemErrorException(error);
        
        assertTrue(exception.getMessage().contains("System error occurred"));
        assertTrue(exception.getMessage().contains("OutOfMemoryError"));
        assertTrue(exception.getMessage().contains("Java heap space"));
        assertEquals(error, exception.getCause());
        assertEquals("SYSTEM_ERROR", exception.getErrorCode());
        assertEquals("CRITICAL", exception.getSeverity());
        assertEquals("ERROR", exception.getCategory());
        assertNotNull(exception.getErrorId());
    }
    
    @Test
    void testConstructorWithMessage() {
        String message = "Critical system failure";
        SystemErrorException exception = new SystemErrorException(message);
        
        assertEquals(message, exception.getMessage());
        assertEquals("SYSTEM_ERROR", exception.getErrorCode());
        assertEquals("CRITICAL", exception.getSeverity());
        assertEquals("ERROR", exception.getCategory());
        assertNotNull(exception.getErrorId());
    }
    
    @Test
    void testConstructorWithMessageAndError() {
        String message = "Memory allocation failed";
        OutOfMemoryError error = new OutOfMemoryError("Heap space exhausted");
        SystemErrorException exception = new SystemErrorException(message, error);
        
        assertEquals(message, exception.getMessage());
        assertEquals(error, exception.getCause());
        assertEquals("SYSTEM_ERROR", exception.getErrorCode());
        assertEquals("CRITICAL", exception.getSeverity());
    }
    
    @Test
    void testOutOfMemoryFactory() {
        String component = "ImageProcessor";
        long memoryRequested = 1024 * 1024 * 1024; // 1GB
        OutOfMemoryError cause = new OutOfMemoryError("Java heap space");
        
        SystemErrorException exception = SystemErrorException.outOfMemory(component, memoryRequested, cause);
        
        assertTrue(exception.getMessage().contains("Out of memory"));
        assertTrue(exception.getMessage().contains(component));
        assertTrue(exception.getMessage().contains("1073741824"));
        assertEquals(cause, exception.getCause());
        assertEquals(component, exception.getContext().getContextData("component"));
        assertEquals(memoryRequested, exception.getContext().getContextData("memoryRequested"));
        assertEquals("memory", exception.getContext().getMetadata("errorType"));
    }
    
    @Test
    void testStackOverflowFactory() {
        String methodName = "recursiveMethod";
        int stackDepth = 5000;
        StackOverflowError cause = new StackOverflowError();
        
        SystemErrorException exception = SystemErrorException.stackOverflow(methodName, stackDepth, cause);
        
        assertTrue(exception.getMessage().contains("Stack overflow"));
        assertTrue(exception.getMessage().contains(methodName));
        assertTrue(exception.getMessage().contains("5000"));
        assertEquals(cause, exception.getCause());
        assertEquals(methodName, exception.getContext().getContextData("methodName"));
        assertEquals(stackDepth, exception.getContext().getContextData("stackDepth"));
        assertEquals("stack", exception.getContext().getMetadata("errorType"));
    }
    
    @Test
    void testThreadDeathFactory() {
        String threadName = "WorkerThread-1";
        ThreadDeath cause = new ThreadDeath();
        
        SystemErrorException exception = SystemErrorException.threadDeath(threadName, cause);
        
        assertTrue(exception.getMessage().contains("Thread death"));
        assertTrue(exception.getMessage().contains(threadName));
        assertEquals(cause, exception.getCause());
        assertEquals(threadName, exception.getContext().getContextData("threadName"));
        assertEquals("thread", exception.getContext().getMetadata("errorType"));
    }
    
    @Test
    void testClassLoadingErrorFactory() {
        String className = "com.example.MissingClass";
        NoClassDefFoundError cause = new NoClassDefFoundError("com/example/MissingClass");
        
        SystemErrorException exception = SystemErrorException.classLoadingError(className, cause);
        
        assertTrue(exception.getMessage().contains("Class loading error"));
        assertTrue(exception.getMessage().contains(className));
        assertEquals(cause, exception.getCause());
        assertEquals(className, exception.getContext().getContextData("className"));
        assertEquals("classloading", exception.getContext().getMetadata("errorType"));
    }
    
    @Test
    void testCategoryAndSeverity() {
        SystemErrorException exception = new SystemErrorException("Test error");
        
        assertEquals("ERROR", exception.getCategory());
        assertEquals("CRITICAL", exception.getSeverity());
    }
    
    @Test
    void testDefaultErrorCode() {
        SystemErrorException exception = new SystemErrorException("Test error");
        assertEquals("SYSTEM_ERROR", exception.getErrorCode());
    }
    
    @Test
    void testToString() {
        SystemErrorException exception = new SystemErrorException("System failure");
        String toString = exception.toString();
        
        assertTrue(toString.contains("SystemErrorException"));
        assertTrue(toString.contains("SYSTEM_ERROR"));
        assertTrue(toString.contains("System failure"));
        assertTrue(toString.contains(exception.getErrorId()));
    }
    
    @Test
    void testWithNullError() {
        SystemErrorException exception = new SystemErrorException((Error) null);
        
        assertTrue(exception.getMessage().contains("System error occurred"));
        assertNull(exception.getCause());
        assertEquals("SYSTEM_ERROR", exception.getErrorCode());
    }
    
    @Test
    void testWithDifferentErrorTypes() {
        // Test with various Error types
        VirtualMachineError vmError = new OutOfMemoryError("VM error");
        SystemErrorException vmException = new SystemErrorException(vmError);
        assertEquals(vmError, vmException.getCause());
        
        LinkageError linkageError = new NoClassDefFoundError("Linkage error");
        SystemErrorException linkageException = new SystemErrorException(linkageError);
        assertEquals(linkageError, linkageException.getCause());
    }
}