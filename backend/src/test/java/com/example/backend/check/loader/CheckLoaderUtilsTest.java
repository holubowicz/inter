package com.example.backend.check.loader;

import com.example.backend.check.common.exception.filepath.FilepathEmptyException;
import com.example.backend.check.common.exception.filepath.FilepathNullException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckLoaderUtilsTest {

    @Test
    void getCheckNameFromPath_whenFilepathIsNull_thenThrowsFilepathNullException() {
        assertThrows(FilepathNullException.class, () ->
                CheckLoaderUtils.getCheckNameFromPath(null)
        );
    }

    @Test
    void getCheckNameFromPath_whenFilepathIsEmpty_thenThrowsFilepathEmptyException() {
        assertThrows(FilepathEmptyException.class, () ->
                CheckLoaderUtils.getCheckNameFromPath(Paths.get(""))
        );
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