package com.example.backend.check.common.exception;

public class CheckInputDTOListNullException extends RuntimeException {

    public static final String MESSAGE = "Check input DTO list is null";

    public CheckInputDTOListNullException() {
        super(MESSAGE);
    }

}
