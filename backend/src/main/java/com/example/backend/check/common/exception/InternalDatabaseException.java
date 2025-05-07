package com.example.backend.check.common.exception;

import static com.example.backend.check.common.error.message.DatabaseErrorMessage.FAILED_QUERY_INTERNAL_DB;

public class InternalDatabaseException extends RuntimeException {

    public InternalDatabaseException(Throwable cause) {
        super(FAILED_QUERY_INTERNAL_DB, cause);
    }

}
