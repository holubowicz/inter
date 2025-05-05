package com.example.backend.check.common.exception.query;

public class QueryEmptyException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Query is empty";

    public QueryEmptyException() {
        super(DEFAULT_MESSAGE);
    }

}
