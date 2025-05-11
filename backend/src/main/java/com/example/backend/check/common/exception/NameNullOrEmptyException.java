package com.example.backend.check.common.exception;

public class NameNullOrEmptyException extends RuntimeException {

    public static final String MESSAGE = "Name is null";

    public NameNullOrEmptyException() {
        super(MESSAGE);
    }

}
