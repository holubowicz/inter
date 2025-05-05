package com.example.backend.check.common.exception.filepath;

public class FilepathNullException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Filepath is null";

    public FilepathNullException() {
        super(DEFAULT_MESSAGE);
    }

}
