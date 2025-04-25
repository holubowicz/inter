package com.example.backend.check.loader;

import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckDto;
import com.example.backend.check.model.factory.CheckFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Component
public class CheckLoader {

    private final CheckLoaderConfiguration checkLoaderConfiguration;

    @Autowired
    public CheckLoader(CheckLoaderConfiguration checkLoaderConfiguration) {
        this.checkLoaderConfiguration = checkLoaderConfiguration;
    }

    public List<CheckDto> getCheckDtoList() {
        Path checksPath = Paths.get(this.checkLoaderConfiguration.getChecksPath())
                .toAbsolutePath();

        if (!Files.exists(checksPath)) {
            throw new RuntimeException("Check directory does not exist");
        }

        try {
            return Files.walk(checksPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(CheckLoaderUtils.CHECK_FILE_EXTENSION))
                    .map(this::getCheck)
                    .map(CheckDto::from)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load checks", e);
        }
    }

    public Check convertCheckDtoToCheck(CheckDto checkDto) {
        if (checkDto == null) {
            log.error("Check DTO is null");
        }

        if (checkDto.getName() == null || checkDto.getName().isEmpty()) {
            return CheckFactory.createErrorCheck(CheckFactory.CHECK_DTO_INCORRECT_ERROR);
        }

        String filename = checkDto.getName() + CheckLoaderUtils.CHECK_FILE_EXTENSION;
        Path filepath = Paths.get(this.checkLoaderConfiguration.getChecksPath(), filename);
        
        return getCheck(filepath);
    }

    public Check getCheck(Path filepath) {
        String queryName = CheckLoaderUtils.getCheckNameFromPath(filepath);

        try {
            String content = Files.readString(filepath);
            return CheckFactory.createCheck(queryName, content);
        } catch (Exception e) {
            return CheckFactory.createNameErrorCheck(queryName, CheckFactory.FAILED_TO_LOAD_CONTENT_ERROR);
        }
    }

}
