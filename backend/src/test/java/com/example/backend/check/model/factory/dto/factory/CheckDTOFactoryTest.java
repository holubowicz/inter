package com.example.backend.check.model.factory.dto.factory;

import com.example.backend.check.model.dto.CheckDTO;
import org.junit.jupiter.api.Test;

import static com.example.backend.check.model.dto.factory.CheckDTOFactory.createNameCheckDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CheckDTOFactoryTest {

    @Test
    void createNameCheckDTO_whenCheckNameProvided_thenReturnsCheckDTO() {
        String checkName = "check-name";

        CheckDTO checkDTO = createNameCheckDTO(checkName);

        assertNotNull(checkDTO);
        assertEquals(checkName, checkDTO.getName());
    }

}