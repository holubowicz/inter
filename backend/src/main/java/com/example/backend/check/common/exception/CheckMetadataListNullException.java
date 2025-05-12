package com.example.backend.check.common.exception;

public class CheckMetadataListNullException extends RuntimeException {

    public static final String MESSAGE = "Check metadata list is null";

    public CheckMetadataListNullException() {
        super(MESSAGE);
    }

}
