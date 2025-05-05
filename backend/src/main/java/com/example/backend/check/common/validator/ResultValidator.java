package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.ResultNullException;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public final class ResultValidator {

    public static void validate(BigDecimal result) {
        if (result == null) {
            throw new ResultNullException();
        }
    }

}
