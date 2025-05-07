package com.example.backend.check.model.dto.factory;

import com.example.backend.check.model.dto.CheckDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CheckDTOFactory {

    public static CheckDTO createNameCheckDTO(String checkName) {
        return CheckDTO.builder().name(checkName).build();
    }

}
