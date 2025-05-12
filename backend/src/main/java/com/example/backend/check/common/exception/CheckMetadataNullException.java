package com.example.backend.check.common.exception;

public class CheckMetadataNullException extends RuntimeException {

    public static final String MESSAGE = "Check metadata is null";

    public CheckMetadataNullException() {
        super(MESSAGE);
    }
}
