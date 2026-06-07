package com.bibik.task4.exception;

public class ValidationException extends Exception {

    private final String fieldName;

    public ValidationException(String message) {
        super(message);
        this.fieldName = "unknown";
    }

    public ValidationException(String message, String fieldName) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
