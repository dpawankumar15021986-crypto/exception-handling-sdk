package com.sdk.exceptions.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Context information for exceptions.
 * Provides thread-safe storage for additional metadata.
 */
public class ExceptionContext {

    private final Map<String, Object> contextData;
    // This field was implicitly present in the original changes but not in the provided original code.
    // Assuming it's intended for metadata storage as per the builder pattern.
    private final Map<String, Object> metadata;

    /**
     * Default constructor.
     */
    public ExceptionContext() {
        this.contextData = new ConcurrentHashMap<>();
        this.metadata = new ConcurrentHashMap<>();
    }

    /**
     * Constructor with initial context data and metadata.
     *
     * @param contextData initial context data
     * @param metadata initial metadata
     */
    public ExceptionContext(Map<String, Object> contextData, Map<String, Object> metadata) {
        this.contextData = contextData != null ? new HashMap<>(contextData) : new HashMap<>();
        this.metadata = metadata != null ? new HashMap<>(metadata) : new HashMap<>();
    }

    /**
     * Adds a context entry.
     *
     * @param key the key
     * @param value the value
     * @return this context for chaining
     */
    public ExceptionContext add(String key, Object value) {
        contextData.put(key, value);
        return this;
    }

    /**
     * Gets a context value.
     *
     * @param key the key
     * @return the value, or null if not found
     */
    public Object get(String key) {
        return contextData.get(key);
    }

    /**
     * Gets a context value by key (alias for get method for backward compatibility).
     *
     * @param key the key
     * @return the value, or null if not found
     */
    public Object getContextData(String key) {
        return contextData.get(key);
    }

    /**
     * Adds context data (alias for add method for backward compatibility).
     *
     * @param key the key
     * @param value the value
     * @return this context for chaining
     */
    public ExceptionContext addContextData(String key, Object value) {
        return add(key, value);
    }

    /**
     * Gets metadata value by key.
     *
     * @param key the key
     * @return the metadata value, or null if not found
     */
    public String getMetadata(String key) {
        Object value = metadata.get(key); // Changed to use 'metadata' map
        return value != null ? value.toString() : null;
    }

    /**
     * Adds metadata.
     *
     * @param key the key
     * @param value the value
     * @return this context for chaining
     */
    public ExceptionContext addMetadata(String key, String value) {
        metadata.put(key, value); // Changed to use 'metadata' map
        return this;
    }

    /**
     * Gets all context data (non-metadata entries).
     *
     * @return map of all context data
     */
    public Map<String, Object> getAllContextData() {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : contextData.entrySet()) {
            // Assuming metadata keys are not intended to be in getAllContextData
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * Gets all metadata entries.
     *
     * @return map of all metadata
     */
    public Map<String, String> getAllMetadata() {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : metadata.entrySet()) { // Changed to use 'metadata' map
            result.put(entry.getKey(), entry.getValue().toString());
        }
        return result;
    }

    /**
     * Gets a context value with type casting.
     *
     * @param key the key
     * @param type the expected type
     * @param <T> the type parameter
     * @return the value cast to the specified type, or null if not found or wrong type
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object value = contextData.get(key);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    /**
     * Checks if a key exists in the context.
     *
     * @param key the key
     * @return true if the key exists, false otherwise
     */
    public boolean contains(String key) {
        return contextData.containsKey(key);
    }

    /**
     * Removes a context entry.
     *
     * @param key the key
     * @return the removed value, or null if not found
     */
    public Object remove(String key) {
        return contextData.remove(key);
    }

    /**
     * Clears all context data.
     */
    public void clear() {
        contextData.clear();
        metadata.clear(); // Added to clear metadata as well
    }

    /**
     * Gets the size of the context.
     *
     * @return the number of context entries
     */
    public int size() {
        return contextData.size(); // This now only returns contextData size.
                                   // If total size is needed, it should be contextData.size() + metadata.size()
    }

    /**
     * Checks if the context is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return contextData.isEmpty() && metadata.isEmpty(); // Check both maps
    }

    /**
     * Gets a copy of the context data.
     *
     * @return a copy of the context data
     */
    public Map<String, Object> getContextData() {
        return new HashMap<>(contextData);
    }

    /**
     * Gets all keys in the context.
     *
     * @return set of keys
     */
    public java.util.Set<String> getKeys() {
        return contextData.keySet();
    }

    /**
     * Creates a new builder instance.
     *
     * @return a new ExceptionContext.Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for ExceptionContext.
     */
    public static class Builder {
        private Map<String, Object> contextData = new HashMap<>();
        private Map<String, Object> metadata = new HashMap<>();

        /**
         * Adds context data.
         *
         * @param key the key
         * @param value the value
         * @return this builder
         */
        public Builder addContextData(String key, Object value) {
            this.contextData.put(key, value);
            return this;
        }

        /**
         * Adds metadata.
         *
         * @param key the key
         * @param value the value
         * @return this builder
         */
        public Builder addMetadata(String key, Object value) {
            this.metadata.put(key, value);
            return this;
        }

        /**
         * Builds the ExceptionContext.
         *
         * @return the built ExceptionContext
         */
        public ExceptionContext build() {
            return new ExceptionContext(contextData, metadata);
        }
    }

    @Override
    public String toString() {
        return "ExceptionContext{" +
                "contextData=" + contextData +
                ", metadata=" + metadata +
                '}';
    }
}