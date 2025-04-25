package com.example.backend.check.loader;

import java.nio.file.Path;

public class CheckLoaderUtils {

    public static final String CHECK_FILE_EXTENSION = ".sql";

    public static String getCheckNameFromPath(Path filepath) {
        if (filepath == null) {
            throw new NullPointerException("The filepath is null");
        }

        String pathString = filepath.toString();
        if (pathString.isEmpty()) {
            throw new RuntimeException("The filepath is empty");
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
