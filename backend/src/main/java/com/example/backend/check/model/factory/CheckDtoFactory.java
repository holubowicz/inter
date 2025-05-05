package com.example.backend.check.model.factory;

import com.example.backend.check.common.validator.NameValidator;
import com.example.backend.check.model.CheckDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CheckDtoFactory {

    public static CheckDto createNameCheckDto(String checkName) {
        NameValidator.validate(checkName);
        return CheckDto.builder().name(checkName).build();
    }

}
