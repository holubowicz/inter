package com.example.backend.check.common.exception;

public class FilepathNullOrEmptyException extends RuntimeException {

    public static final String MESSAGE = "Filepath is null or empty";

    public FilepathNullOrEmptyException() {
        super(MESSAGE);
    }

}
