package ru.pusk.common.error;

public record FieldIncorrectError(String fieldName, String reason){
    public FieldIncorrectError(String fieldName) {
        this(fieldName, null);
    }
}
