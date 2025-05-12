package com.example.backend.check.loader;

import com.example.backend.check.common.exception.io.CheckFilenameWrongException;
import com.example.backend.check.model.CheckMetadata;
import lombok.experimental.UtilityClass;

import java.nio.file.Path;

@UtilityClass
public final class CheckLoaderUtils {

    public static final String CHECK_FILE_EXTENSION = ".sql";

    public static CheckMetadata getCheckMetadataFromPath(Path filepath) {
        String filename = filepath.getFileName().toString().trim();

        if (filename.toLowerCase().endsWith(CHECK_FILE_EXTENSION)) {
            filename = filename.substring(0, filename.length() - CHECK_FILE_EXTENSION.length());
        }

        String[] filenameParts = filename.split("\\.");

        if (filenameParts.length == 0 || filenameParts.length > 2) {
            throw new CheckFilenameWrongException();
        }

        if (filenameParts.length == 1) {
            return CheckMetadata.builder().name(filenameParts[0]).build();
        }

        return new CheckMetadata(filenameParts[0], filenameParts[1]);
    }

}
