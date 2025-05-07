package com.example.backend.check.common.exception;

public class NameNullOrEmptyException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Name is null";

    public NameNullOrEmptyException() {
        super(DEFAULT_MESSAGE);
    }

}
