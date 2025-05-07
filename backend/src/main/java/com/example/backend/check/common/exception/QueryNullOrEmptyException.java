package com.example.backend.check.common.exception;

public class QueryNullOrEmptyException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Query is null or empty";

    public QueryNullOrEmptyException() {
        super(DEFAULT_MESSAGE);
    }

}
