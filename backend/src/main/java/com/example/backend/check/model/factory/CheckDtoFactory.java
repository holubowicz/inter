package com.example.backend.check.model.factory;

import com.example.backend.check.model.CheckDto;
import lombok.experimental.UtilityClass;

import static com.example.backend.check.common.ErrorMessages.CHECK_NAME_EMPTY;
import static com.example.backend.check.common.ErrorMessages.CHECK_NAME_NULL;

@UtilityClass
public final class CheckDtoFactory {

    public static CheckDto createNameCheckDto(String checkName) {
        if (checkName == null) {
            throw new IllegalArgumentException(CHECK_NAME_NULL);
        }
        if (checkName.isEmpty()) {
            throw new IllegalArgumentException(CHECK_NAME_EMPTY);
        }

        return CheckDto.builder().name(checkName).build();
    }

}
