package com.example.backend.check.loader;

import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
    void getCheckDtoList_whenCorrectCheckPathSet_thenReturnListOfCheckDto() {
        List<CheckDto> result = underTest.getCheckDtoList();

        assertFalse(result.isEmpty());
        result.forEach(checkDto -> {
            assertNotNull(checkDto);
            assertNotNull(checkDto.getName());
            assertFalse(checkDto.getName().isEmpty());
        });
    }

    @Test
    void getCheckDtoList_whenIncorrectCheckPathSet_thenThrowRuntimeException() {
        checkLoaderConfiguration.setChecksPath("path/that/does/not/exist");

        Exception exception = assertThrows(RuntimeException.class, underTest::getCheckDtoList);

        assertTrue(exception.getMessage().contains(CheckLoader.CHECK_DIRECTORY_DONT_EXIST_ERROR));
    }


    @Test
    void convertCheckDtoToCheck_whenCheckDtoIsNull_thenThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.convertCheckDtoToCheck(null)
        );

        assertTrue(exception.getMessage().contains(CheckLoader.CHECK_DTO_NULL_ERROR));
    }

    @Test
    void convertCheckDtoToCheck_whenCheckDtoNameIsValid_thenReturnCheck() {
        CheckDto checkDto = new CheckDto("absolute-avg");

        Check check = underTest.convertCheckDtoToCheck(checkDto);

        assertNotNull(check);
        assertEquals("absolute-avg", check.getName());
        assertNotNull(check.getQuery());
        assertNull(check.getError());
    }

    @Test
    void convertCheckDtoToCheck_whenCheckDtoNameIsInvalid_thenReturnErrorCheck() {
        CheckDto checkDto = new CheckDto("not-exist");

        Check check = underTest.convertCheckDtoToCheck(checkDto);

        assertNotNull(check);
        assertEquals("not-exist", check.getName());
        assertNull(check.getQuery());
        assertNotNull(check.getError());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void convertCheckDtoToCheck_whenCheckDtoNameIsIncorrect_thenReturnErrorCheck(String checkName) {
        CheckDto checkDto = new CheckDto(checkName);

        Check check = underTest.convertCheckDtoToCheck(checkDto);

        assertNotNull(check);
        assertNull(check.getQuery());
        assertEquals(CheckLoader.CHECK_DTO_INCORRECT_ERROR, check.getError());
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

    @Test
    void getCheck_whenFilepathIsNull_thenReturnCheck() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.getCheck(null)
        );

        assertTrue(exception.getMessage().contains(CheckLoader.FILEPATH_NULL_ERROR));
    }

    @Test
    void getCheck_whenFilepathIsEmpty_thenReturnCheck() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                underTest.getCheck(Paths.get(""))
        );

        assertTrue(exception.getMessage().contains(CheckLoaderUtils.FILEPATH_EMPTY_ERROR));
    }

}