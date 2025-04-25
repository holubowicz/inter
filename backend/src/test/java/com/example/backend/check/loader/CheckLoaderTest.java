package com.example.backend.check.loader;

import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckDto;
import com.example.backend.check.model.factory.CheckFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CheckLoaderTest {

    private static final String checksPathString = "src/test/resources/com/example/backend/check/checks";
    private static final CheckLoaderConfiguration checkLoaderConfiguration = new CheckLoaderConfiguration();
    private static final CheckLoader underTest = new CheckLoader(checkLoaderConfiguration);

    @BeforeEach
    void setupBeforeEach() {
        checkLoaderConfiguration.setChecksPath(checksPathString);
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

        String expectedMessage = "Check directory does not exist";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void convertCheckDtoToCheck_whenCheckDtoIsNull_thenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                underTest.convertCheckDtoToCheck(null)
        );
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
        assertEquals(CheckFactory.CHECK_DTO_INCORRECT_ERROR, check.getError());
    }

    @Test
    void getCheck_whenFilepathIsValid_thenReturnCorrectCheck() {
        Path filepath = Paths.get(checksPathString + "/absolute-avg.sql");

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
        Exception exception = assertThrows(NullPointerException.class, () ->
                underTest.getCheck(null)
        );

        String expectedMessage = "The filepath is null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getCheck_whenFilepathIsEmpty_thenReturnCheck() {
        Exception exception = assertThrows(RuntimeException.class, () ->
                underTest.getCheck(Paths.get(""))
        );

        String expectedMessage = "The filepath is empty";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}