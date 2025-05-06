package com.example.backend.check.model.factory.dto.factory;

import com.example.backend.check.common.exception.name.NameEmptyException;
import com.example.backend.check.common.exception.name.NameNullException;
import com.example.backend.check.model.dto.CheckDTO;
import org.junit.jupiter.api.Test;

import static com.example.backend.check.model.dto.factory.CheckDTOFactory.createNameCheckDTO;
import static org.junit.jupiter.api.Assertions.*;

class CheckDTOFactoryTest {

    @Test
    void createNameCheckDTO_whenCheckNameIsNull_thenThrowsNameNullException() {
        assertThrows(NameNullException.class, () -> createNameCheckDTO(null));
    }

    @Test
    void createNameCheckDTO_whenCheckNameIsEmpty_thenThrowsNameEmptyException() {
        String checkName = "";

        assertThrows(NameEmptyException.class, () -> createNameCheckDTO(checkName));
    }

    @Test
    void createNameCheckDTO_whenCheckNameProvided_thenReturnsCheckDTO() {
        String checkName = "check-name";

        CheckDTO checkDTO = createNameCheckDTO(checkName);

        assertNotNull(checkDTO);
        assertEquals(checkName, checkDTO.getName());
    }

}