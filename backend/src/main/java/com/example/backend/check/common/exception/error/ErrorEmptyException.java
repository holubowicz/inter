package com.example.backend.check.common.exception.error;

public class ErrorEmptyException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Error is empty";

    public ErrorEmptyException() {
        super(DEFAULT_MESSAGE);
    }

}
