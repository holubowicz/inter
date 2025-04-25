package com.example.backend.check.loader;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

@Slf4j
public class CheckLoaderUtils {

    public static final String CHECK_FILE_EXTENSION = ".sql";

    public static final String FILEPATH_IS_EMPTY_ERROR = "The filepath is empty";

    public static String getCheckNameFromPath(Path filepath) {
        if (filepath == null) {
            log.error("The filepath is null");
        }

        String pathString = filepath.toString();
        if (pathString.isEmpty()) {
            throw new IllegalArgumentException(FILEPATH_IS_EMPTY_ERROR);
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
