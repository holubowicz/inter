package com.example.backend.check.common.validator;

import com.example.backend.check.common.exception.TimestampNullException;
import lombok.experimental.UtilityClass;

import java.time.Instant;

@UtilityClass
public final class TimestampValidator {

    public static void validate(Instant timestamp) {
        if (timestamp == null) {
            throw new TimestampNullException();
        }
    }

}
