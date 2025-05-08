package com.example.backend.check.common.exception;

public class InternalDatabaseException extends RuntimeException {

    public static final String FAILED_QUERY_INTERNAL_DB = "Failed to query internal database";

    public InternalDatabaseException(Throwable cause) {
        super(FAILED_QUERY_INTERNAL_DB, cause);
    }

}
