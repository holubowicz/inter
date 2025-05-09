package com.example.backend.check.loader;

import com.example.backend.check.common.exception.CheckInputDTONullException;
import com.example.backend.check.common.exception.FilepathNullOrEmptyException;
import com.example.backend.check.common.exception.QueryNullOrEmptyException;
import com.example.backend.check.common.exception.io.CheckDirectoryNotFoundException;
import com.example.backend.check.common.exception.io.ChecksNotLoadedException;
import com.example.backend.check.model.Check;
import com.example.backend.check.model.dto.CheckInputDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.example.backend.check.common.error.message.LoadingErrorMessage.CHECK_INPUT_DTO_INCORRECT;
import static com.example.backend.check.common.error.message.LoadingErrorMessage.FAILED_TO_LOAD_CONTENT;
import static com.example.backend.check.model.factory.CheckFactory.*;


@Slf4j
@Component
@AllArgsConstructor
public class CheckLoader {

    private final CheckLoaderConfiguration checkLoaderConfiguration;

    public List<String> getCheckNames() {
        Path checksPath = Paths.get(this.checkLoaderConfiguration.getChecksPath())
                .toAbsolutePath();

        if (!Files.exists(checksPath)) {
            throw new CheckDirectoryNotFoundException();
        }

        try {
            return Files.walk(checksPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(CheckLoaderUtils.CHECK_FILE_EXTENSION))
                    .map(CheckLoaderUtils::getCheckNameFromPath)
                    .sorted()
                    .toList();
        } catch (SecurityException | IOException e) {
            throw new ChecksNotLoadedException(e);
        }
    }

    public Check convertIntoCheck(CheckInputDTO checkInputDTO) {
        if (checkInputDTO == null) {
            throw new CheckInputDTONullException();
        }

        if (checkInputDTO.getName() == null || checkInputDTO.getName().isEmpty()) {
            return createErrorCheck(CHECK_INPUT_DTO_INCORRECT);
        }

        String filename = checkInputDTO.getName() + CheckLoaderUtils.CHECK_FILE_EXTENSION;
        Path filepath = Paths.get(this.checkLoaderConfiguration.getChecksPath(), filename);

        return getCheck(filepath);
    }

    public Check getCheck(Path filepath) {
        if (filepath == null || filepath.toString().trim().isEmpty()) {
            throw new FilepathNullOrEmptyException();
        }

        String checkName = CheckLoaderUtils.getCheckNameFromPath(filepath);

        try {
            String query = Files.readString(filepath);
            if (query.trim().isEmpty()) {
                throw new QueryNullOrEmptyException();
            }

            return createCheck(checkName, query);
        } catch (Exception e) {
            log.warn(FAILED_TO_LOAD_CONTENT);
            return createNameErrorCheck(checkName, FAILED_TO_LOAD_CONTENT);
        }
    }

}
