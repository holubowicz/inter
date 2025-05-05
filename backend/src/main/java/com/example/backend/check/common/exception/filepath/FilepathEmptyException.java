package com.example.backend.check.common.exception.filepath;

public class FilepathEmptyException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Filepath is empty";

    public FilepathEmptyException() {
        super(DEFAULT_MESSAGE);
    }

}
