package com.example.backend.check.loader;

import com.example.backend.check.common.exception.filepath.FilepathEmptyException;
import com.example.backend.check.common.validator.FilepathValidator;
import lombok.experimental.UtilityClass;

import java.nio.file.Path;

@UtilityClass
public final class CheckLoaderUtils {

    public static final String CHECK_FILE_EXTENSION = ".sql";

    public static String getCheckNameFromPath(Path filepath) {
        FilepathValidator.validate(filepath);
        if (filepath.toString().trim().isEmpty()) {
            throw new FilepathEmptyException();
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
