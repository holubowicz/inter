package com.example.backend.check.common.exception.name;

public class NameNullException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Name is null";

    public NameNullException() {
        super(DEFAULT_MESSAGE);
    }

}
