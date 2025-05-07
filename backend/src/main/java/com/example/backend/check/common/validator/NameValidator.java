package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.NameNullOrEmptyException;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class NameValidator {

    public static void validate(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new NameNullOrEmptyException();
        }
    }

}
