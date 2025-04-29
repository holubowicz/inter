package com.example.backend.check.loader;

import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckInputDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.example.backend.check.CheckErrorMessages.*;
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
    void getCheckNameList_whenIncorrectChecksPathSet_thenThrowRuntimeException() {
        checkLoaderConfiguration.setChecksPath("path/that/does/not/exist");

        Exception exception = assertThrows(RuntimeException.class, underTest::getCheckNameList);

        assertTrue(exception.getMessage().contains(CHECK_DIRECTORY_DONT_EXIST));
    }

    @Test
    void getCheckNameList_whenCorrectChecksPathSet_thenReturnListOfCheckName() {
        List<String> result = underTest.getCheckNameList();

        assertFalse(result.isEmpty());
        result.forEach(checkName -> {
            assertNotNull(checkName);
            assertFalse(checkName.isEmpty());
        });
    }


    @Test
    void convertCheckInputDtoToCheck_whenCheckInputDtoIsNull_thenThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.convertCheckInputDtoToCheck(null)
        );

        assertTrue(exception.getMessage().contains(CHECK_INPUT_DTO_NULL));
    }

    @Test
    void convertCheckInputDtoToCheck_whenCheckInputDtoNameIsValid_thenReturnCheck() {
        CheckInputDto checkInputDto = new CheckInputDto("absolute-avg");

        Check check = underTest.convertCheckInputDtoToCheck(checkInputDto);

        assertNotNull(check);
        assertEquals("absolute-avg", check.getName());
        assertNotNull(check.getQuery());
        assertNull(check.getError());
    }

    @Test
    void convertCheckInputDtoToCheck_whenCheckInputDtoNameIsInvalid_thenReturnErrorCheck() {
        CheckInputDto checkInputDto = new CheckInputDto("not-exist");

        Check check = underTest.convertCheckInputDtoToCheck(checkInputDto);

        assertNotNull(check);
        assertEquals("not-exist", check.getName());
        assertNull(check.getQuery());
        assertNotNull(check.getError());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void convertCheckInputDtoToCheck_whenCheckInputDtoNameIsIncorrect_thenReturnErrorCheck(String checkName) {
        CheckInputDto checkInputDto = new CheckInputDto(checkName);

        Check check = underTest.convertCheckInputDtoToCheck(checkInputDto);

        assertNotNull(check);
        assertNull(check.getQuery());
        assertEquals(CHECK_INPUT_DTO_INCORRECT, check.getError());
    }


    @Test
    void getCheck_whenFilepathIsNull_thenReturnCheck() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.getCheck(null)
        );

        assertTrue(exception.getMessage().contains(FILEPATH_NULL));
    }

    @Test
    void getCheck_whenFilepathIsEmpty_thenReturnCheck() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.getCheck(Paths.get(""))
        );

        assertTrue(exception.getMessage().contains(FILEPATH_EMPTY));
    }

    @Test
    void getCheck_whenFilepathIsValid_thenReturnCorrectCheck() {
        Path filepath = Paths.get(checksPath + "/absolute-avg.sql");

        Check result = underTest.getCheck(filepath);

        assertEquals("absolute-avg", result.getName());
        assertNotNull(result.getQuery());
        assertNull(result.getError());
    }

    @Test
    void getCheck_whenFilepathInvalid_thenReturnErrorCheck() {
        Path filepath = Paths.get("path/that/does/not/exist/check-not-exist");

        Check result = underTest.getCheck(filepath);

        assertEquals("check-not-exist", result.getName());
        assertNull(result.getQuery());
        assertNotNull(result.getError());
    }

}