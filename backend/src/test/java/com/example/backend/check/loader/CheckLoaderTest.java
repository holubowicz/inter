package com.example.backend.check.loader;

import com.example.backend.check.common.exception.CheckMetadataNullException;
import com.example.backend.check.common.exception.FilepathNullOrEmptyException;
import com.example.backend.check.common.exception.io.CheckDirectoryNotFoundException;
import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.example.backend.check.loader.CheckLoaderUtils.CHECK_FILE_EXTENSION;
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
    void getCheckMetadataList_whenIncorrectChecksPathSet_thenThrowCheckDirectoryNotFoundException() {
        checkLoaderConfiguration.setChecksPath("path/that/does/not/exist");

        assertThrows(CheckDirectoryNotFoundException.class, underTest::getCheckMetadataList);
    }

    @Test
    void getCheckMetadataList_whenCorrectChecksPathSet_thenReturnsListOfCheckName() {
        List<CheckMetadata> result = underTest.getCheckMetadataList();

        assertFalse(result.isEmpty());
        result.forEach(metadata -> {
            assertNotNull(metadata);
            assertFalse(metadata.getName().isEmpty());
            assertFalse(metadata.getCategory().isEmpty());
        });
    }


    @Test
    void convertIntoCheck_whenCheckMetadataIsNull_thenThrowsCheckMetadataNullException() {
        assertThrows(CheckMetadataNullException.class, () ->
                underTest.convertIntoCheck(null)
        );
    }

    @Test
    void convertIntoCheck_whenCheckMetadataNameIsValid_thenReturnsCheck() {
        CheckMetadata checkInputDTO = new CheckMetadata("absolute-avg", "good");

        Check check = underTest.convertIntoCheck(checkInputDTO);

        assertNotNull(check);
        assertEquals("absolute-avg", check.getMetadata().getName());
        assertEquals("good", check.getMetadata().getCategory());
        assertNotNull(check.getQuery());
        assertNull(check.getError());
    }

    @Test
    void convertIntoCheck_whenCheckMetadataNameIsInvalid_thenReturnsErrorCheck() {
        CheckMetadata checkInputDTO = new CheckMetadata("not-exist", "bad");

        Check check = underTest.convertIntoCheck(checkInputDTO);

        assertNotNull(check);
        assertEquals("not-exist", check.getMetadata().getName());
        assertEquals("bad", check.getMetadata().getCategory());
        assertNull(check.getQuery());
        assertNotNull(check.getError());
    }


    @Test
    void getCheck_whenFilepathIsNull_thenThrowsFilepathNullOrEmptyException() {
        CheckMetadata metadata = new CheckMetadata("absolute-avg", "good");

        assertThrows(FilepathNullOrEmptyException.class, () ->
                underTest.getCheck(metadata, null)
        );
    }

    @Test
    void getCheck_whenFilepathIsEmpty_thenThrowsFilepathNullOrEmptyException() {
        CheckMetadata metadata = new CheckMetadata("absolute-avg", "good");

        assertThrows(FilepathNullOrEmptyException.class, () ->
                underTest.getCheck(metadata, Paths.get(""))
        );
    }

    @Test
    void getCheck_whenFilepathInvalid_thenReturnsErrorCheck() {
        CheckMetadata metadata = new CheckMetadata("check-not-exist", "bad");
        Path filepath = Paths.get(
                "path/that/does/not/exist/" +
                        "/" +
                        metadata.getName() +
                        "." +
                        metadata.getCategory()
        );

        Check result = underTest.getCheck(metadata, filepath);

        assertEquals(metadata.getName(), result.getMetadata().getName());
        assertEquals(metadata.getCategory(), result.getMetadata().getCategory());
        assertNull(result.getQuery());
        assertNotNull(result.getError());
    }

    @Test
    void getCheck_whenFilepathIsValidButDifferentCase_thenReturnsCorrectCheck() {
        CheckMetadata metadata = new CheckMetadata("Absolute-Avg", "gOOd");
        Path filepath = Paths.get(
                checksPath +
                        "/" +
                        metadata.getName() +
                        "." +
                        metadata.getCategory() +
                        CHECK_FILE_EXTENSION
        );

        Check result = underTest.getCheck(metadata, filepath);

        assertEquals(metadata.getName(), result.getMetadata().getName());
        assertEquals(metadata.getCategory(), result.getMetadata().getCategory());
        assertNotNull(result.getQuery());
        assertNull(result.getError());
    }

    @Test
    void getCheck_whenFilepathIsValid_thenReturnsCorrectCheck() {
        CheckMetadata metadata = new CheckMetadata("absolute-avg", "good");
        Path filepath = Paths.get(
                checksPath +
                        "/" +
                        metadata.getName() +
                        "." +
                        metadata.getCategory() +
                        CHECK_FILE_EXTENSION
        );

        Check result = underTest.getCheck(metadata, filepath);

        assertEquals(metadata.getName(), result.getMetadata().getName());
        assertEquals(metadata.getCategory(), result.getMetadata().getCategory());
        assertNotNull(result.getQuery());
        assertNull(result.getError());
    }

}