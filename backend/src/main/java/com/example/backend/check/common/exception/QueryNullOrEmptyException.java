package com.example.backend.check.common.exception;

public class QueryNullOrEmptyException extends RuntimeException {

    public static final String MESSAGE = "Query is null or empty";

    public QueryNullOrEmptyException() {
        super(MESSAGE);
    }

}
