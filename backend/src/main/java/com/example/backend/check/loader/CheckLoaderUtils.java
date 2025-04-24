package com.example.backend.check.loader;

import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class CheckLoaderUtils {

    public final String checkFileExtension = ".sql";

    public String getCheckNameFromPath(Path filepath) {
        if (filepath == null) {
            throw new NullPointerException("The filepath is null");
        }

        String pathString = filepath.toString();
        if (pathString.isEmpty()) {
            throw new RuntimeException("The path is empty");
        }

        String filename = filepath.getFileName().toString();

        String lowerFilename = filename.toLowerCase();
        int dotIdx = lowerFilename.indexOf('.');

        if (dotIdx > 0) {
            return filename.substring(0, dotIdx);
        }

        if (lowerFilename.endsWith(checkFileExtension)) {
            return filename.substring(0, lowerFilename.length() - checkFileExtension.length());
        }

        return filename;
    }

}
