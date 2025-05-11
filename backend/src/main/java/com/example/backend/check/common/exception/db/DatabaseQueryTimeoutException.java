package com.example.backend.check.common.exception.db;

public class DatabaseQueryTimeoutException extends RuntimeException {

    public static final String MESSAGE = "Query has timed out";

    public DatabaseQueryTimeoutException(Throwable cause) {
        super(MESSAGE, cause);
    }

}
