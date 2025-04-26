package com.example.backend.check.loader;

import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckDto;
import com.example.backend.check.model.factory.CheckFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class CheckLoader {

    public static final String CHECK_DTO_NULL_ERROR = "Check DTO is null";
    public static final String CHECK_DTO_INCORRECT_ERROR = "Check DTO is incorrect";
    public static final String FILEPATH_NULL_ERROR = "File path is null";
    public static final String FAILED_TO_LOAD_CONTENT_ERROR = "Failed to load check content";
    public static final String CHECK_DIRECTORY_DONT_EXIST_ERROR = "Check directory does not exist";
    public static final String FAILED_TO_LOAD_CHECKS_ERROR = "Failed to load checks";

    private final CheckLoaderConfiguration checkLoaderConfiguration;

    @Autowired
    public CheckLoader(CheckLoaderConfiguration checkLoaderConfiguration) {
        this.checkLoaderConfiguration = checkLoaderConfiguration;
    }

    public List<CheckDto> getCheckDtoList() {
        Path checksPath = Paths.get(this.checkLoaderConfiguration.getChecksPath())
                .toAbsolutePath();

        if (!Files.exists(checksPath)) {
            throw new RuntimeException(CHECK_DIRECTORY_DONT_EXIST_ERROR);
        }

        try {
            return Files.walk(checksPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(CheckLoaderUtils.CHECK_FILE_EXTENSION))
                    .map(this::getCheck)
                    .map(CheckDto::from)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException(FAILED_TO_LOAD_CHECKS_ERROR);
        }
    }

    public Check convertCheckDtoToCheck(CheckDto checkDto) {
        if (checkDto == null) {
            throw new IllegalArgumentException(CHECK_DTO_NULL_ERROR);
        }

        if (checkDto.getName() == null || checkDto.getName().isEmpty()) {
            return CheckFactory.createErrorCheck(CHECK_DTO_INCORRECT_ERROR);
        }

        String filename = checkDto.getName() + CheckLoaderUtils.CHECK_FILE_EXTENSION;
        Path filepath = Paths.get(this.checkLoaderConfiguration.getChecksPath(), filename);

        return getCheck(filepath);
    }

    public Check getCheck(Path filepath) {
        if (filepath == null) {
            throw new IllegalArgumentException(FILEPATH_NULL_ERROR);
        }

        String queryName = CheckLoaderUtils.getCheckNameFromPath(filepath);

        try {
            String content = Files.readString(filepath);
            return CheckFactory.createCheck(queryName, content);
        } catch (Exception e) {
            return CheckFactory.createNameErrorCheck(queryName, FAILED_TO_LOAD_CONTENT_ERROR);
        }
    }

}
