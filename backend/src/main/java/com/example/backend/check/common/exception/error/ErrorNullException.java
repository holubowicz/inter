package com.example.backend.check.common.exception.error;

public class ErrorNullException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Error is null";

    public ErrorNullException() {
        super(DEFAULT_MESSAGE);
    }

}
