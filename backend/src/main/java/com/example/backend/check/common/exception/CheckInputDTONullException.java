package com.example.backend.check.common.exception;

public class CheckInputDTONullException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Check input DTO is null";

    public CheckInputDTONullException() {
        super(DEFAULT_MESSAGE);
    }
}
