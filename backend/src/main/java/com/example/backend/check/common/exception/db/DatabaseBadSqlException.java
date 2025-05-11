package com.example.backend.check.common.exception.db;

public class DatabaseBadSqlException extends RuntimeException {

    public static final String MESSAGE = "Provided SQL query is invalid";

    public DatabaseBadSqlException(Throwable cause) {
        super(MESSAGE, cause);
    }

}
