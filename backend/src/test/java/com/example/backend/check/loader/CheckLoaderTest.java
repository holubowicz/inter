package com.example.backend.check.loader;

import com.example.backend.check.common.exception.CheckInputDTONullException;
import com.example.backend.check.common.exception.FilepathNullOrEmptyException;
import com.example.backend.check.common.exception.io.CheckDirectoryNotFoundException;
import com.example.backend.check.model.Check;
import com.example.backend.check.model.dto.CheckInputDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.example.backend.check.common.error.message.LoadingErrorMessage.CHECK_INPUT_DTO_INCORRECT;
import static org.junit.jupiter.api.Assertions.*;

class CheckLoaderTest {

    // TODO: load the checksPath from application properties
    private static final String checksPath = "src/test/resources/checks";
    private static final CheckLoaderConfiguration checkLoaderConfiguration = new CheckLoaderConfiguration();

    private static final CheckLoader underTest = new CheckLoader(checkLoaderConfiguration);

    @BeforeEach
    void setupBeforeEach() {
        checkLoaderConfiguration.setChecksPath(checksPath);
    }


    @Test
    void getCheckNameList_whenIncorrectChecksPathSet_thenThrowCheckDirectoryNotFoundException() {
        checkLoaderConfiguration.setChecksPath("path/that/does/not/exist");

        assertThrows(CheckDirectoryNotFoundException.class, underTest::getCheckNames);
    }

    @Test
    void getCheckNameList_whenCorrectChecksPathSet_thenReturnsListOfCheckName() {
        List<String> result = underTest.getCheckNames();

        assertFalse(result.isEmpty());
        result.forEach(checkName -> {
            assertNotNull(checkName);
            assertFalse(checkName.isEmpty());
        });
    }


    @Test
    void convertIntoCheck_whenCheckInputDTOIsNull_thenThrowsCheckInputDTONullException() {
        assertThrows(CheckInputDTONullException.class, () ->
                underTest.convertIntoCheck(null)
        );
    }

    @Test
    void convertIntoCheck_whenCheckInputDTONameIsValid_thenReturnsCheck() {
        CheckInputDTO checkInputDTO = new CheckInputDTO("absolute-avg");

        Check check = underTest.convertIntoCheck(checkInputDTO);

        assertNotNull(check);
        assertEquals("absolute-avg", check.getName());
        assertNotNull(check.getQuery());
        assertNull(check.getError());
    }

    @Test
    void convertIntoCheck_whenCheckInputDTONameIsInvalid_thenReturnsErrorCheck() {
        CheckInputDTO checkInputDTO = new CheckInputDTO("not-exist");

        Check check = underTest.convertIntoCheck(checkInputDTO);

        assertNotNull(check);
        assertEquals("not-exist", check.getName());
        assertNull(check.getQuery());
        assertNotNull(check.getError());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void convertIntoCheck_whenCheckInputDTONameIsIncorrect_thenReturnsErrorCheck(String checkName) {
        CheckInputDTO checkInputDTO = new CheckInputDTO(checkName);

        Check check = underTest.convertIntoCheck(checkInputDTO);

        assertNotNull(check);
        assertNull(check.getQuery());
        assertEquals(CHECK_INPUT_DTO_INCORRECT, check.getError());
    }


    @ParameterizedTest
    @NullAndEmptySource
    void getCheck_whenFilepathIsNull_thenThrowsFilepathNullException(String filepathString) {
        assertThrows(FilepathNullOrEmptyException.class, () ->
                underTest.getCheck(Paths.get(filepathString))
        );
    }

    @Test
    void getCheck_whenFilepathIsValid_thenReturnsCorrectCheck() {
        Path filepath = Paths.get(checksPath + "/absolute-avg.sql");

        Check result = underTest.getCheck(filepath);

        assertEquals("absolute-avg", result.getName());
        assertNotNull(result.getQuery());
        assertNull(result.getError());
    }

    @Test
    void getCheck_whenFilepathInvalid_thenReturnsErrorCheck() {
        Path filepath = Paths.get("path/that/does/not/exist/check-not-exist");

        Check result = underTest.getCheck(filepath);

        assertEquals("check-not-exist", result.getName());
        assertNull(result.getQuery());
        assertNotNull(result.getError());
    }

}