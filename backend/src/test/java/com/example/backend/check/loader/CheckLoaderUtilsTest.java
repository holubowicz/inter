package com.example.backend.check.loader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CheckLoaderUtilsTest {

    private final CheckLoaderUtils underTest = new CheckLoaderUtils();

    @Test
    void getCheckNameFromPath_whenPathIsValid_thenReturnsCheckName() {
        Path path = Paths.get("/path/to/folder/check.sql");

        String result = underTest.getCheckNameFromPath(path);

        assertEquals("check", result);
    }

    @Test
    void getCheckNameFromPath_whenPathIsNull_thenThrowsException() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            underTest.getCheckNameFromPath(null);
        });

        String expectedMessage = "The filepath is null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getCheckNameFromPath_whenPathIsEmpty_thenThrowsException() {
        Exception exception = assertThrows(RuntimeException.class, () ->
                underTest.getCheckNameFromPath(Paths.get(""))
        );

        String expectedMessage = "The filepath is empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getCheckNameFromPath_whenNoExtension_thenReturnsCheckName() {
        Path path = Paths.get("/path/to/folder/check");

        String result = underTest.getCheckNameFromPath(path);

        assertEquals("check", result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/path/to/folder/check.sql", "/path/to/folder/check.SQL"})
    void getCheckNameFromPath_whenExtensionLowercase_thenReturnsCheckName(String pathString) {
        Path path = Paths.get(pathString);

        String result = underTest.getCheckNameFromPath(path);

        assertEquals("check", result);
    }

    @Test
    void getCheckNameFromPath_whenFilenameContainsTwoDots_thenReturnsCheckName() {
        Path path = Paths.get("/path/to/folder/check.test.sql");

        String result = underTest.getCheckNameFromPath(path);

        assertEquals("check", result);
    }

    @Test
    void getCheckNameFromPath_whenExtensionIsTest_thenReturnsCheckName() {
        Path path = Paths.get("/path/to/folder/check.test");

        String result = underTest.getCheckNameFromPath(path);

        assertEquals("check", result);
    }

    @Test
    void getCheckNameFromPath_whenContainsSpaceInFilename_thenReturnsCheckName() {
        Path path = Paths.get("/path/to/folder/check name.sql");

        String result = underTest.getCheckNameFromPath(path);

        assertEquals("check name", result);
    }

    @Test
    void getCheckNameFromPath_whenFilenameCapitalized_thenReturnsCheckName() {
        Path path = Paths.get("/path/to/folder/Check.sql");

        String result = underTest.getCheckNameFromPath(path);

        assertEquals("Check", result);
    }

}