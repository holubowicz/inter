package com.example.backend.check.common.exception.db;

public class InternalDatabaseException extends RuntimeException {

    public static final String MESSAGE = "Failed to query internal database";

    public InternalDatabaseException(Throwable cause) {
        super(MESSAGE, cause);
    }

}
