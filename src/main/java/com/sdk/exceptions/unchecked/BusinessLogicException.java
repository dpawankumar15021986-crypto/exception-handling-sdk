package com.sdk.exceptions.unchecked;

import com.sdk.exceptions.core.ExceptionContext;

/**
 * Exception thrown when business logic rules are violated.
 * Represents domain-specific errors that violate business constraints.
 */
public class BusinessLogicException extends UncheckedException {
    
    public static final String DEFAULT_ERROR_CODE = "BUSINESS_LOGIC_ERROR";
    public static final String SEVERITY = "HIGH";
    
    private final String businessRule;
    
    /**
     * Constructs a BusinessLogicException with the specified message.
     *
     * @param message the detail message
     */
    public BusinessLogicException(String message) {
        super(DEFAULT_ERROR_CODE, message);
        this.businessRule = null;
    }
    
    /**
     * Constructs a BusinessLogicException with business rule information.
     *
     * @param businessRule the business rule that was violated
     * @param message the detail message
     */
    public BusinessLogicException(String businessRule, String message) {
        super(DEFAULT_ERROR_CODE, 
              String.format("Business rule violation [%s]: %s", businessRule, message),
              null,
              new ExceptionContext()
                  .addContextData("businessRule", businessRule)
                  .addMetadata("violationType", "business_rule"));
        this.businessRule = businessRule;
    }
    
    /**
     * Constructs a BusinessLogicException with business rule and entity information.
     *
     * @param businessRule the business rule that was violated
     * @param entityType the type of entity involved
     * @param entityId the ID of the entity involved
     * @param message the detail message
     */
    public BusinessLogicException(String businessRule, String entityType, String entityId, String message) {
        super(DEFAULT_ERROR_CODE,
              String.format("Business rule violation [%s] for %s[%s]: %s", businessRule, entityType, entityId, message),
              null,
              new ExceptionContext()
                  .addContextData("businessRule", businessRule)
                  .addContextData("entityType", entityType)
                  .addContextData("entityId", entityId)
                  .addMetadata("violationType", "entity_constraint"));
        this.businessRule = businessRule;
    }
    
    /**
     * Constructs a BusinessLogicException for state transition errors.
     *
     * @param entityType the type of entity
     * @param entityId the ID of the entity
     * @param currentState the current state
     * @param targetState the attempted target state
     * @param allowedStates the allowed target states
     */
    public BusinessLogicException(String entityType, String entityId, String currentState, 
                                String targetState, java.util.Set<String> allowedStates) {
        super(DEFAULT_ERROR_CODE,
              String.format("Invalid state transition for %s[%s]: cannot transition from '%s' to '%s'. Allowed states: %s",
                      entityType, entityId, currentState, targetState, allowedStates),
              null,
              new ExceptionContext()
                  .addContextData("entityType", entityType)
                  .addContextData("entityId", entityId)
                  .addContextData("currentState", currentState)
                  .addContextData("targetState", targetState)
                  .addContextData("allowedStates", allowedStates)
                  .addMetadata("violationType", "state_transition"));
        this.businessRule = "STATE_TRANSITION_RULE";
    }
    
    /**
     * Gets the business rule that was violated.
     *
     * @return the business rule, or null if not specified
     */
    public String getBusinessRule() {
        return businessRule;
    }
    
    @Override
    public String getSeverity() {
        return SEVERITY;
    }
}
