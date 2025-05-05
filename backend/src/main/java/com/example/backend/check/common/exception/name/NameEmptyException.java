package com.example.backend.check.common.exception.name;

public class NameEmptyException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Name is empty";

    public NameEmptyException() {
        super(DEFAULT_MESSAGE);
    }

}
