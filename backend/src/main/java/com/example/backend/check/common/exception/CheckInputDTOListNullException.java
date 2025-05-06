package com.example.backend.check.common.exception;

public class CheckInputDTOListNullException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Check input DTO list is null";

    public CheckInputDTOListNullException() {
        super(DEFAULT_MESSAGE);
    }

}
