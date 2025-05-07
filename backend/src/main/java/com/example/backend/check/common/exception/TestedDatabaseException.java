package com.example.backend.check.common.exception;

import static com.example.backend.check.common.error.message.DatabaseErrorMessage.FAILED_QUERY_TESTED_DB;

public class TestedDatabaseException extends RuntimeException {

    public TestedDatabaseException(Throwable cause) {
        super(FAILED_QUERY_TESTED_DB, cause);
    }

}
