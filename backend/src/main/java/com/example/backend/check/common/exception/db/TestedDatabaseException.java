package com.example.backend.check.common.exception.db;

public class TestedDatabaseException extends RuntimeException {

    public static final String FAILED_QUERY_TESTED_DB = "Failed to query tested database";

    public TestedDatabaseException(Throwable cause) {
        super(FAILED_QUERY_TESTED_DB, cause);
    }

}
