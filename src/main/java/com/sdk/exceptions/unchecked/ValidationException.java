package com.sdk.exceptions.unchecked;

import com.sdk.exceptions.core.ExceptionContext;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Exception thrown when validation fails.
 * Supports multiple validation errors and detailed field-level information.
 */
public class ValidationException extends UncheckedException {

    public static final String DEFAULT_ERROR_CODE = "VALIDATION_ERROR";
    public static final String SEVERITY = "MEDIUM";

    private final List<ValidationError> validationErrors;

    /**
     * Constructs a ValidationException with the specified message.
     *
     * @param message the detail message
     */
    public ValidationException(String message) {
        super(DEFAULT_ERROR_CODE, message);
        this.validationErrors = new ArrayList<>();
    }

    /**
     * Constructs a ValidationException with validation errors.
     *
     * @param validationErrors the list of validation errors
     */
    public ValidationException(List<ValidationError> validationErrors) {
        super(DEFAULT_ERROR_CODE, 
              String.format("Validation failed with %d error(s)", validationErrors.size()),
              null,
              createContextFromErrors(validationErrors));
        this.validationErrors = new ArrayList<>(validationErrors);
    }

    /**
     * Constructs a ValidationException for a single field.
     *
     * @param field the field that failed validation
     * @param message the validation error message
     * @param rejectedValue the value that was rejected
     */
    public ValidationException(String field, String message, Object rejectedValue) {
        super(DEFAULT_ERROR_CODE,
              String.format("Validation failed for field '%s': %s", field, message),
              null,
              new ExceptionContext()
                  .addContextData("field", field)
                  .addContextData("rejectedValue", rejectedValue)
                  .addMetadata("validationType", "field"));
        this.validationErrors = List.of(new ValidationError(field, message, rejectedValue));
    }

    /**
     * Constructs a ValidationException with message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public ValidationException(String message, Throwable cause) {
        super(DEFAULT_ERROR_CODE, message, cause);
        this.validationErrors = new ArrayList<>();
    }

    /**
     * Constructs a ValidationException with all parameters.
     *
     * @param errorCode the error code
     * @param message the detail message
     * @param cause the cause of this exception
     * @param context additional context information
     */
    public ValidationException(String errorCode, String message, Throwable cause, ExceptionContext context) {
        super(errorCode, message, cause, context);
        this.validationErrors = new ArrayList<>();
    }

    /**
     * Creates a validation exception for field validation failure.
     *
     * @param fieldName the name of the field
     * @param invalidValue the invalid value
     * @param reason the reason for validation failure
     * @return ValidationException instance
     */
    public static ValidationException fieldValidation(String fieldName, Object invalidValue, String reason) {
        ExceptionContext context = new ExceptionContext();
        context.addContextData("fieldName", fieldName);
        context.addContextData("invalidValue", invalidValue);
        context.addContextData("validationError", reason);
        context.addMetadata("validationType", "field");

        return new ValidationException(DEFAULT_ERROR_CODE,
                String.format("Field validation failed for '%s': %s", fieldName, reason),
                null,
                context);
    }

    /**
     * Creates a validation exception for required field missing.
     *
     * @param fieldName the name of the required field
     * @return ValidationException instance
     */
    public static ValidationException requiredField(String fieldName) {
        ExceptionContext context = new ExceptionContext();
        context.addContextData("fieldName", fieldName);
        context.addMetadata("validationType", "required");

        return new ValidationException(DEFAULT_ERROR_CODE,
                String.format("Required field missing: %s", fieldName),
                null,
                context);
    }

    /**
     * Creates a validation exception for multiple field validation failures.
     *
     * @param fieldNames the list of field names that failed validation
     * @return ValidationException instance
     */
    public static ValidationException multipleFields(List<String> fieldNames) {
        ExceptionContext context = new ExceptionContext();
        context.addContextData("fieldNames", fieldNames);
        context.addMetadata("validationType", "multiple");

        String fieldList = fieldNames != null && !fieldNames.isEmpty() 
            ? String.join(", ", fieldNames) 
            : "none";

        return new ValidationException(DEFAULT_ERROR_CODE,
                String.format("Multiple field validation failed for: %s", fieldList),
                null,
                context);
    }

    /**
     * Creates a validation exception for business rule violation.
     *
     * @param ruleName the name of the business rule
     * @param ruleDescription the description of the rule
     * @return ValidationException instance
     */
    public static ValidationException businessRule(String ruleName, String ruleDescription) {
        ExceptionContext context = new ExceptionContext();
        context.addContextData("ruleName", ruleName);
        context.addContextData("ruleDescription", ruleDescription);
        context.addMetadata("validationType", "business-rule");

        return new ValidationException(DEFAULT_ERROR_CODE,
                String.format("Business rule validation failed: %s - %s", ruleName, ruleDescription),
                null,
                context);
    }

    /**
     * Gets the list of validation errors.
     *
     * @return unmodifiable list of validation errors
     */
    public List<ValidationError> getValidationErrors() {
        return Collections.unmodifiableList(validationErrors);
    }

    /**
     * Adds a validation error.
     *
     * @param error the validation error to add
     */
    public void addValidationError(ValidationError error) {
        validationErrors.add(error);
    }

    /**
     * Adds a validation error for a field.
     *
     * @param field the field name
     * @param message the error message
     * @param rejectedValue the rejected value
     */
    public void addValidationError(String field, String message, Object rejectedValue) {
        validationErrors.add(new ValidationError(field, message, rejectedValue));
    }

    /**
     * Checks if there are validation errors for a specific field.
     *
     * @param field the field name
     * @return true if there are errors for the field, false otherwise
     */
    public boolean hasErrorsForField(String field) {
        return validationErrors.stream()
                .anyMatch(error -> field.equals(error.getField()));
    }

    /**
     * Gets validation errors for a specific field.
     *
     * @param field the field name
     * @return a list of validation errors for the field
     */
    public List<ValidationError> getErrorsForField(String field) {
        return validationErrors.stream()
                .filter(error -> field.equals(error.getField()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public String getSeverity() {
        return SEVERITY;
    }

    private static ExceptionContext createContextFromErrors(List<ValidationError> errors) {
        ExceptionContext context = new ExceptionContext();
        context.addContextData("validationErrorCount", errors.size());
        context.addMetadata("validationType", "multi-field");

        for (int i = 0; i < errors.size(); i++) {
            ValidationError error = errors.get(i);
            context.addContextData("error_" + i + "_field", error.getField());
            context.addContextData("error_" + i + "_message", error.getMessage());
            context.addContextData("error_" + i + "_rejectedValue", error.getRejectedValue());
        }

        return context;
    }

    /**
     * Represents a single validation error.
     */
    public static class ValidationError {
        private final String field;
        private final String message;
        private final Object rejectedValue;

        public ValidationError(String field, String message, Object rejectedValue) {
            this.field = field;
            this.message = message;
            this.rejectedValue = rejectedValue;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }

        public Object getRejectedValue() {
            return rejectedValue;
        }

        @Override
        public String toString() {
            return String.format("ValidationError{field='%s', message='%s', rejectedValue=%s}", 
                    field, message, rejectedValue);
        }
    }
}