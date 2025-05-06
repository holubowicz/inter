package com.example.backend.check.model.factory;

import com.example.backend.check.common.validator.NameValidator;
import com.example.backend.check.model.CheckDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CheckDTOFactory {

    public static CheckDTO createNameCheckDTO(String checkName) {
        NameValidator.validate(checkName);
        return CheckDTO.builder().name(checkName).build();
    }

}
