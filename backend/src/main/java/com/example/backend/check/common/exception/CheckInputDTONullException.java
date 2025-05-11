package com.example.backend.check.common.exception;

public class CheckInputDTONullException extends RuntimeException {

    public static final String MESSAGE = "Check input DTO is null";

    public CheckInputDTONullException() {
        super(MESSAGE);
    }
}
