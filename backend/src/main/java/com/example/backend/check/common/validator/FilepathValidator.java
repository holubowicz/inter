package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.filepath.FilepathNullException;
import lombok.experimental.UtilityClass;

import java.nio.file.Path;

@UtilityClass
public final class FilepathValidator {

    public static void validate(Path filepath) {
        if (filepath == null) {
            throw new FilepathNullException();
        }
    }

}
