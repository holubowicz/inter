package com.example.backend.check.common.exception;

public class FilepathNullOrEmptyException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Filepath is null or empty";

    public FilepathNullOrEmptyException() {
        super(DEFAULT_MESSAGE);
    }

}
