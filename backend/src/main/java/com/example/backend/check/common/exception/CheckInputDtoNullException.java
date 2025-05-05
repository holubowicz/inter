package com.example.backend.check.common.exception;

public class CheckInputDtoNullException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Check input DTO is null";

    public CheckInputDtoNullException() {
        super(DEFAULT_MESSAGE);
    }
}
