package com.example.backend.check.common.exception;

public class ResultNullException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Result is null";

    public ResultNullException() {
        super(DEFAULT_MESSAGE);
    }

}
