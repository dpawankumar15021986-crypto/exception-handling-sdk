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

    /**
     * Default constructor.
     */
    public ExceptionContext() {
        this.contextData = new ConcurrentHashMap<>();
    }

    /**
     * Constructor with initial context data.
     *
     * @param initialData initial context data
     */
    public ExceptionContext(Map<String, Object> initialData) {
        this.contextData = new ConcurrentHashMap<>();
        if (initialData != null) {
            this.contextData.putAll(initialData);
        }
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
    }

    /**
     * Gets the size of the context.
     *
     * @return the number of context entries
     */
    public int size() {
        return contextData.size();
    }

    /**
     * Checks if the context is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return contextData.isEmpty();
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

    @Override
    public String toString() {
        return "ExceptionContext{" +
                "contextData=" + contextData +
                '}';
    }
}