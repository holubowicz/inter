package com.example.backend.check.common.exception.db;

public class TestedDatabaseException extends RuntimeException {

    public static final String MESSAGE = "Failed to query tested database";

    public TestedDatabaseException(Throwable cause) {
        super(MESSAGE, cause);
    }

}
