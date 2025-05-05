package com.example.backend.check.common.exception;

public class TimestampNullException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Timestamp is null";

    public TimestampNullException() {
        super(DEFAULT_MESSAGE);
    }

}
