package com.example.backend.check.common.exception;

public class ExecutionTimeNullException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Execution time is null";

    public ExecutionTimeNullException() {
        super(DEFAULT_MESSAGE);
    }

}
