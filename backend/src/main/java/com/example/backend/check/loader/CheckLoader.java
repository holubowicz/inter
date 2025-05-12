package com.example.backend.check.loader;

import com.example.backend.check.common.exception.CheckMetadataNullException;
import com.example.backend.check.common.exception.FilepathNullOrEmptyException;
import com.example.backend.check.common.exception.QueryNullOrEmptyException;
import com.example.backend.check.common.exception.io.CheckDirectoryNotFoundException;
import com.example.backend.check.common.exception.io.ChecksNotLoadedException;
import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckMetadata;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.example.backend.check.common.error.message.LoadingErrorMessage.CHECK_METADATA_INCORRECT;
import static com.example.backend.check.common.error.message.LoadingErrorMessage.FAILED_TO_LOAD_CONTENT;
import static com.example.backend.check.loader.CheckLoaderUtils.CHECK_FILE_EXTENSION;
import static com.example.backend.check.model.factory.CheckFactory.*;


@Slf4j
@Component
@AllArgsConstructor
public class CheckLoader {

    private final CheckLoaderConfiguration checkLoaderConfiguration;

    public List<CheckMetadata> getCheckMetadataList() {
        Path checksPath = Paths.get(this.checkLoaderConfiguration.getChecksPath())
                .toAbsolutePath();

        if (!Files.exists(checksPath)) {
            throw new CheckDirectoryNotFoundException();
        }

        try {
            return Files.walk(checksPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(CHECK_FILE_EXTENSION))
                    .map(CheckLoaderUtils::getCheckMetadataFromPath)
                    .toList();
        } catch (Exception e) {
            throw new ChecksNotLoadedException(e);
        }
    }

    public Check convertIntoCheck(CheckMetadata metadata) {
        if (metadata == null) {
            throw new CheckMetadataNullException();
        }

        if (metadata.getName() == null || metadata.getName().isEmpty()) {
            return createErrorCheck(CHECK_METADATA_INCORRECT);
        }

        StringBuilder filenameBuilder = new StringBuilder(metadata.getName());
        if (metadata.getCategory() != null) {
            filenameBuilder.append(".").append(metadata.getCategory());
        }
        filenameBuilder.append(CHECK_FILE_EXTENSION);

        Path filepath = resolveCaseInsensitivePath(
                this.checkLoaderConfiguration.getChecksPath(),
                filenameBuilder.toString()
        );
        return getCheck(metadata, filepath);
    }

    public Check getCheck(CheckMetadata metadata, Path filepath) {
        if (filepath == null || filepath.toString().trim().isEmpty()) {
            throw new FilepathNullOrEmptyException();
        }

        try {
            String query = Files.readString(filepath);
            if (query.trim().isEmpty()) {
                throw new QueryNullOrEmptyException();
            }

            return createCheck(metadata, query);
        } catch (Exception e) {
            log.warn(FAILED_TO_LOAD_CONTENT);
            return createNameErrorCheck(metadata, FAILED_TO_LOAD_CONTENT);
        }
    }

    private Path resolveCaseInsensitivePath(String directory, String filename) {
        try (Stream<Path> files = Files.list(Paths.get(directory))) {
            Optional<Path> match = files
                    .filter(path -> path.getFileName().toString().equalsIgnoreCase(filename))
                    .findFirst();
            if (match.isPresent()) {
                return match.get();
            }
        } catch (Exception ignored) {
            log.info("Could not resolve path or any path variations: {}/{}", directory, filename);
        }
        return Paths.get(directory, filename);
    }

}
