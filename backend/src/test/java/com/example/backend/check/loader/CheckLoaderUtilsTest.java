package com.example.backend.check.loader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CheckLoaderUtilsTest {

    @Test
    void getCheckNameFromPath_whenFilepathIsNull_thenThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            CheckLoaderUtils.getCheckNameFromPath(null);
        });

        assertTrue(exception.getMessage().contains(CheckLoaderUtils.FILEPATH_NULL_ERROR));
    }

    @Test
    void getCheckNameFromPath_whenFilepathIsEmpty_thenThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CheckLoaderUtils.getCheckNameFromPath(Paths.get(""))
        );

        assertTrue(exception.getMessage().contains(CheckLoaderUtils.FILEPATH_EMPTY_ERROR));
    }

    @Test
    void getCheckNameFromPath_whenFilepathIsValid_thenReturnsCheckName() {
        Path path = Paths.get("/path/to/folder/check.sql");

        String result = CheckLoaderUtils.getCheckNameFromPath(path);

        assertEquals("check", result);
    }

    @Test
    void getCheckNameFromPath_whenNoExtension_thenReturnsCheckName() {
        Path path = Paths.get("/path/to/folder/check");

        String result = CheckLoaderUtils.getCheckNameFromPath(path);

        assertEquals("check", result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/path/to/folder/check.sql", "/path/to/folder/check.SQL"})
    void getCheckNameFromPath_whenExtensionLowercase_thenReturnsCheckName(String pathString) {
        Path path = Paths.get(pathString);

        String result = CheckLoaderUtils.getCheckNameFromPath(path);

        assertEquals("check", result);
    }

    @Test
    void getCheckNameFromPath_whenFilenameContainsTwoDots_thenReturnsCheckName() {
        Path path = Paths.get("/path/to/folder/check.test.sql");

        String result = CheckLoaderUtils.getCheckNameFromPath(path);

        assertEquals("check", result);
    }

    @Test
    void getCheckNameFromPath_whenExtensionIsTest_thenReturnsCheckName() {
        Path path = Paths.get("/path/to/folder/check.test");

        String result = CheckLoaderUtils.getCheckNameFromPath(path);

        assertEquals("check", result);
    }

    @Test
    void getCheckNameFromPath_whenContainsSpaceInFilename_thenReturnsCheckName() {
        Path path = Paths.get("/path/to/folder/check name.sql");

        String result = CheckLoaderUtils.getCheckNameFromPath(path);

        assertEquals("check name", result);
    }

    @Test
    void getCheckNameFromPath_whenFilenameCapitalized_thenReturnsCheckName() {
        Path path = Paths.get("/path/to/folder/Check.sql");

        String result = CheckLoaderUtils.getCheckNameFromPath(path);

        assertEquals("Check", result);
    }

}