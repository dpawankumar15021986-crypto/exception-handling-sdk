package com.sdk.exceptions.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Context information for exceptions.
 * Provides thread-safe storage for additional metadata and context data.
 */
public class ExceptionContext {
    
    private final Map<String, Object> contextData;
    private final Map<String, String> metadata;
    
    /**
     * Constructs an empty ExceptionContext.
     */
    public ExceptionContext() {
        this.contextData = new ConcurrentHashMap<>();
        this.metadata = new ConcurrentHashMap<>();
    }
    
    /**
     * Constructs an ExceptionContext with initial context data.
     *
     * @param contextData the initial context data
     */
    public ExceptionContext(Map<String, Object> contextData) {
        this();
        if (contextData != null) {
            this.contextData.putAll(contextData);
        }
    }
    
    /**
     * Adds context data.
     *
     * @param key the key
     * @param value the value
     * @return this context for method chaining
     */
    public ExceptionContext addContextData(String key, Object value) {
        Objects.requireNonNull(key, "Key cannot be null");
        contextData.put(key, value);
        return this;
    }
    
    /**
     * Adds metadata.
     *
     * @param key the key
     * @param value the value
     * @return this context for method chaining
     */
    public ExceptionContext addMetadata(String key, String value) {
        Objects.requireNonNull(key, "Key cannot be null");
        metadata.put(key, value);
        return this;
    }
    
    /**
     * Gets context data by key.
     *
     * @param key the key
     * @return the context data value, or null if not found
     */
    public Object getContextData(String key) {
        return contextData.get(key);
    }
    
    /**
     * Gets context data by key with type casting.
     *
     * @param key the key
     * @param type the expected type
     * @param <T> the type parameter
     * @return the context data value cast to the specified type, or null if not found or not castable
     */
    @SuppressWarnings("unchecked")
    public <T> T getContextData(String key, Class<T> type) {
        Object value = contextData.get(key);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }
    
    /**
     * Gets metadata by key.
     *
     * @param key the key
     * @return the metadata value, or null if not found
     */
    public String getMetadata(String key) {
        return metadata.get(key);
    }
    
    /**
     * Gets all context data as an unmodifiable map.
     *
     * @return the context data map
     */
    public Map<String, Object> getAllContextData() {
        return Collections.unmodifiableMap(new HashMap<>(contextData));
    }
    
    /**
     * Gets all metadata as an unmodifiable map.
     *
     * @return the metadata map
     */
    public Map<String, String> getAllMetadata() {
        return Collections.unmodifiableMap(new HashMap<>(metadata));
    }
    
    /**
     * Checks if context data exists for the given key.
     *
     * @param key the key
     * @return true if context data exists, false otherwise
     */
    public boolean hasContextData(String key) {
        return contextData.containsKey(key);
    }
    
    /**
     * Checks if metadata exists for the given key.
     *
     * @param key the key
     * @return true if metadata exists, false otherwise
     */
    public boolean hasMetadata(String key) {
        return metadata.containsKey(key);
    }
    
    /**
     * Clears all context data and metadata.
     */
    public void clear() {
        contextData.clear();
        metadata.clear();
    }
    
    /**
     * Creates a copy of this context.
     *
     * @return a new ExceptionContext with the same data
     */
    public ExceptionContext copy() {
        ExceptionContext copy = new ExceptionContext();
        copy.contextData.putAll(this.contextData);
        copy.metadata.putAll(this.metadata);
        return copy;
    }
    
    @Override
    public String toString() {
        return String.format("ExceptionContext{contextData=%s, metadata=%s}", contextData, metadata);
    }
}
