package com.example.backend.check.common.exception;

public class CheckInputDtoListNullException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Check input DTO list is null";

    public CheckInputDtoListNullException() {
        super(DEFAULT_MESSAGE);
    }

}
