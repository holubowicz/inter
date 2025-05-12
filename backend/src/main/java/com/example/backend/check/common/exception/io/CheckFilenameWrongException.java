package com.example.backend.check.common.exception.io;

public class CheckFilenameWrongException extends RuntimeException {

    public static final String MESSAGE = "Check Filename is wrong";

    public CheckFilenameWrongException() {
        super(MESSAGE);
    }

}
