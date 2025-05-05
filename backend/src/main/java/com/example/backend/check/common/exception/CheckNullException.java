package com.example.backend.check.common.exception;

public class CheckNullException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Check is null";

    public CheckNullException() {
        super(DEFAULT_MESSAGE);
    }

}
