package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.error.ErrorEmptyException;
import com.example.backend.check.common.exception.error.ErrorNullException;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class ErrorValidator {

    public static void validate(String error) {
        if (error == null) {
            throw new ErrorNullException();
        }
        
        if (error.trim().isEmpty()) {
            throw new ErrorEmptyException();
        }
    }

}
