package com.example.backend.check.common.exception.io;

public class CheckDirectoryNotFoundException extends RuntimeException {

    public static final String MESSAGE = "Check directory not found";

    public CheckDirectoryNotFoundException() {
        super(MESSAGE);
    }

}
