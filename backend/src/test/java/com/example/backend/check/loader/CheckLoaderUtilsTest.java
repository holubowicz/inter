package com.example.backend.check.loader;

import com.example.backend.check.model.CheckMetadata;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.example.backend.check.loader.CheckLoaderUtils.CHECK_FILE_EXTENSION;
import static com.example.backend.check.loader.CheckLoaderUtils.getCheckMetadataFromPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckLoaderUtilsTest {

    @Test
    public void getCheckMetadataFromPath_whenFilepathIsValid_thenReturnsCheckMetadata() {
        CheckMetadata expectedMetadata = new CheckMetadata("check-name", "category");
        Path path = Paths.get(
                "/path/to/folder/" +
                        expectedMetadata.getName() +
                        "." +
                        expectedMetadata.getCategory() +
                        CHECK_FILE_EXTENSION
        );

        CheckMetadata metadata = getCheckMetadataFromPath(path);

        assertEquals(expectedMetadata.getName(), metadata.getName());
        assertEquals(expectedMetadata.getCategory(), metadata.getCategory());
    }

    @Test
    public void getCheckMetadataFromPath_whenNoExtension_thenReturnsCheckMetadata() {
        CheckMetadata expectedMetadata = new CheckMetadata("check-name", "category");
        Path path = Paths.get(
                "/path/to/folder/" +
                        expectedMetadata.getName() +
                        "." +
                        expectedMetadata.getCategory()
        );

        CheckMetadata metadata = getCheckMetadataFromPath(path);

        assertEquals(expectedMetadata.getName(), metadata.getName());
        assertEquals(expectedMetadata.getCategory(), metadata.getCategory());
    }

    @Test
    public void getCheckMetadataFromPath_whenExtensionLowercase_thenReturnsCheckMetadata() {
        CheckMetadata expectedMetadata = new CheckMetadata("check-name", "category");
        Path path = Paths.get(
                "/path/to/folder/" +
                        expectedMetadata.getName() +
                        "." +
                        expectedMetadata.getCategory() +
                        CHECK_FILE_EXTENSION.toLowerCase()
        );

        CheckMetadata metadata = getCheckMetadataFromPath(path);

        assertEquals(expectedMetadata.getName(), metadata.getName());
        assertEquals(expectedMetadata.getCategory(), metadata.getCategory());
    }

    @Test
    public void getCheckMetadataFromPath_whenExtensionUppercase_thenReturnsCheckMetadata() {
        CheckMetadata expectedMetadata = new CheckMetadata("check-name", "category");
        Path path = Paths.get(
                "/path/to/folder/" +
                        expectedMetadata.getName() +
                        "." +
                        expectedMetadata.getCategory() +
                        CHECK_FILE_EXTENSION.toUpperCase()
        );

        CheckMetadata metadata = getCheckMetadataFromPath(path);

        assertEquals(expectedMetadata.getName(), metadata.getName());
        assertEquals(expectedMetadata.getCategory(), metadata.getCategory());
    }

    @Test
    public void getCheckMetadataFromPath_whenContainsSpaceInFilename_thenReturnsCheckMetadata() {
        CheckMetadata expectedMetadata = new CheckMetadata("check name", "category");
        Path path = Paths.get(
                "/path/to/folder/" +
                        expectedMetadata.getName() +
                        "." +
                        expectedMetadata.getCategory() +
                        CHECK_FILE_EXTENSION
        );

        CheckMetadata metadata = getCheckMetadataFromPath(path);

        assertEquals(expectedMetadata.getName(), metadata.getName());
        assertEquals(expectedMetadata.getCategory(), metadata.getCategory());
    }

    @Test
    public void getCheckMetadataFromPath_whenFilenameCapitalized_thenReturnsCheckMetadata() {
        CheckMetadata expectedMetadata = new CheckMetadata("Check-Name", "category");
        Path path = Paths.get(
                "/path/to/folder/" +
                        expectedMetadata.getName() +
                        "." +
                        expectedMetadata.getCategory() +
                        CHECK_FILE_EXTENSION
        );

        CheckMetadata metadata = getCheckMetadataFromPath(path);

        assertEquals(expectedMetadata.getName(), metadata.getName());
        assertEquals(expectedMetadata.getCategory(), metadata.getCategory());
    }

}