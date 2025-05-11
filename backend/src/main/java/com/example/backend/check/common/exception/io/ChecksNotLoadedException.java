package com.example.backend.check.common.exception.io;

public class ChecksNotLoadedException extends RuntimeException {

    public static final String MESSAGE = "Failed to load checks";

    public ChecksNotLoadedException(Throwable cause) {
        super(MESSAGE, cause);
    }

}
