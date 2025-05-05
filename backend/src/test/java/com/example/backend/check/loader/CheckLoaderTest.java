package com.example.backend.check.loader;

import com.example.backend.check.common.exception.CheckInputDtoNullException;
import com.example.backend.check.common.exception.filepath.FilepathEmptyException;
import com.example.backend.check.common.exception.filepath.FilepathNullException;
import com.example.backend.check.common.exception.io.CheckDirectoryNotFoundException;
import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckInputDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.example.backend.check.common.ErrorMessages.CHECK_INPUT_DTO_INCORRECT;
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

        assertThrows(CheckDirectoryNotFoundException.class, underTest::getCheckNameList);
    }

    @Test
    void getCheckNameList_whenCorrectChecksPathSet_thenReturnsListOfCheckName() {
        List<String> result = underTest.getCheckNameList();

        assertFalse(result.isEmpty());
        result.forEach(checkName -> {
            assertNotNull(checkName);
            assertFalse(checkName.isEmpty());
        });
    }


    @Test
    void convertCheckInputDtoToCheck_whenCheckInputDtoIsNull_thenThrowsCheckInputDtoNullException() {
        assertThrows(CheckInputDtoNullException.class, () ->
                underTest.convertCheckInputDtoToCheck(null)
        );
    }

    @Test
    void convertCheckInputDtoToCheck_whenCheckInputDtoNameIsValid_thenReturnsCheck() {
        CheckInputDto checkInputDto = new CheckInputDto("absolute-avg");

        Check check = underTest.convertCheckInputDtoToCheck(checkInputDto);

        assertNotNull(check);
        assertEquals("absolute-avg", check.getName());
        assertNotNull(check.getQuery());
        assertNull(check.getError());
    }

    @Test
    void convertCheckInputDtoToCheck_whenCheckInputDtoNameIsInvalid_thenReturnsErrorCheck() {
        CheckInputDto checkInputDto = new CheckInputDto("not-exist");

        Check check = underTest.convertCheckInputDtoToCheck(checkInputDto);

        assertNotNull(check);
        assertEquals("not-exist", check.getName());
        assertNull(check.getQuery());
        assertNotNull(check.getError());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void convertCheckInputDtoToCheck_whenCheckInputDtoNameIsIncorrect_thenReturnsErrorCheck(String checkName) {
        CheckInputDto checkInputDto = new CheckInputDto(checkName);

        Check check = underTest.convertCheckInputDtoToCheck(checkInputDto);

        assertNotNull(check);
        assertNull(check.getQuery());
        assertEquals(CHECK_INPUT_DTO_INCORRECT, check.getError());
    }


    @Test
    void getCheck_whenFilepathIsNull_thenThrowsFilepathNullException() {
        assertThrows(FilepathNullException.class, () ->
                underTest.getCheck(null)
        );
    }

    @Test
    void getCheck_whenFilepathIsEmpty_thenThrowsFilepathEmptyException() {
        assertThrows(FilepathEmptyException.class, () ->
                underTest.getCheck(Paths.get(""))
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