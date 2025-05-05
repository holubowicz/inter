package com.example.backend.check.common.exception.io;

public class ChecksNotLoadedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Failed to load checks";

    public ChecksNotLoadedException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

}
