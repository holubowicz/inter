package com.example.backend.check.common.exception;

public class CheckHistoryNullException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Check history is null";

    public CheckHistoryNullException() {
        super(DEFAULT_MESSAGE);
    }

}
