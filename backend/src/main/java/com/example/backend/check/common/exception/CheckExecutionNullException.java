package com.example.backend.check.common.exception;

public class CheckExecutionNullException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Check execution is null";

    public CheckExecutionNullException() {
        super(DEFAULT_MESSAGE);
    }

}
