package com.example.backend.check.common.exception.query;

public class QueryNullException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Query is null";

    public QueryNullException() {
        super(DEFAULT_MESSAGE);
    }

}
