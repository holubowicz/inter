package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.name.NameEmptyException;
import com.example.backend.check.common.exception.name.NameNullException;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class NameValidator {

    public static void validate(String name) {
        if (name == null) {
            throw new NameNullException();
        }

        if (name.trim().isEmpty()) {
            throw new NameEmptyException();
        }
    }

}
