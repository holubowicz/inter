package com.example.backend.check.loader;

import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckInputDto;
import com.example.backend.check.model.factory.CheckFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.example.backend.check.common.ApiErrorMessages.CHECK_INPUT_DTO_INCORRECT;
import static com.example.backend.check.common.ApiErrorMessages.FAILED_TO_LOAD_CONTENT;
import static com.example.backend.check.common.ErrorMessages.*;


@Slf4j
@Component
public class CheckLoader {

    private final CheckLoaderConfiguration checkLoaderConfiguration;

    @Autowired
    public CheckLoader(CheckLoaderConfiguration checkLoaderConfiguration) {
        this.checkLoaderConfiguration = checkLoaderConfiguration;
    }

    public List<String> getCheckNameList() {
        Path checksPath = Paths.get(this.checkLoaderConfiguration.getChecksPath())
                .toAbsolutePath();

        if (!Files.exists(checksPath)) {
            throw new RuntimeException(CHECK_DIRECTORY_DONT_EXIST);
        }

        try {
            return Files.walk(checksPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(CheckLoaderUtils.CHECK_FILE_EXTENSION))
                    .map(CheckLoaderUtils::getCheckNameFromPath)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException(FAILED_TO_LOAD_CHECKS);
        }
    }

    public Check convertCheckInputDtoToCheck(CheckInputDto checkInputDto) {
        if (checkInputDto == null) {
            throw new IllegalArgumentException(CHECK_INPUT_DTO_NULL);
        }

        if (checkInputDto.getName() == null || checkInputDto.getName().isEmpty()) {
            return CheckFactory.createErrorCheck(CHECK_INPUT_DTO_INCORRECT);
        }

        String filename = checkInputDto.getName() + CheckLoaderUtils.CHECK_FILE_EXTENSION;
        Path filepath = Paths.get(this.checkLoaderConfiguration.getChecksPath(), filename);

        return getCheck(filepath);
    }

    public Check getCheck(Path filepath) {
        if (filepath == null) {
            throw new IllegalArgumentException(FILEPATH_NULL);
        }

        String queryName = CheckLoaderUtils.getCheckNameFromPath(filepath);

        try {
            String content = Files.readString(filepath);
            return CheckFactory.createCheck(queryName, content);
        } catch (Exception e) {
            log.error(FAILED_TO_LOAD_CONTENT);
            return CheckFactory.createNameErrorCheck(queryName, FAILED_TO_LOAD_CONTENT);
        }
    }

}
