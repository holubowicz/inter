package com.example.backend.check.common.exception.io;

public class CheckDirectoryNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Check directory not found";

    public CheckDirectoryNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

}
