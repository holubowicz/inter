package com.example.backend.check.loader;

import java.nio.file.Path;

public class CheckLoaderUtils {

    public static final String CHECK_FILE_EXTENSION = ".sql";

    public static final String FILEPATH_NULL_ERROR = "File path is null";
    public static final String FILEPATH_EMPTY_ERROR = "File path is empty";

    public static String getCheckNameFromPath(Path filepath) {
        if (filepath == null) {
            throw new IllegalArgumentException(FILEPATH_NULL_ERROR);
        }

        String pathString = filepath.toString();
        if (pathString.isEmpty()) {
            throw new IllegalArgumentException(FILEPATH_EMPTY_ERROR);
        }

        String filename = filepath.getFileName().toString();

        String lowerFilename = filename.toLowerCase();
        int dotIdx = lowerFilename.indexOf('.');

        if (dotIdx > 0) {
            return filename.substring(0, dotIdx);
        }

        if (lowerFilename.endsWith(CHECK_FILE_EXTENSION)) {
            return filename.substring(0, lowerFilename.length() - CHECK_FILE_EXTENSION.length());
        }

        return filename;
    }

}
