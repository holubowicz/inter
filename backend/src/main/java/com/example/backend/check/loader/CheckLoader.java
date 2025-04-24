package com.example.backend.check.loader;

import com.example.backend.check.Check;
import com.example.backend.check.CheckDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class CheckLoader {

    private final CheckLoaderConfiguration checkLoaderConfiguration;
    private final CheckLoaderUtils checkLoaderUtils;

    @Autowired
    public CheckLoader(CheckLoaderConfiguration checkLoaderConfiguration, CheckLoaderUtils checkLoaderUtils) {
        this.checkLoaderConfiguration = checkLoaderConfiguration;
        this.checkLoaderUtils = checkLoaderUtils;
    }

    public List<CheckDto> getCheckDtoList() {
        Path checksPath = Paths.get(this.checkLoaderConfiguration.getChecksPath())
                .toAbsolutePath();

        if (!Files.exists(checksPath)) {
            throw new RuntimeException("Check directory does not exist: " + checksPath);
        }

        try {
            return Files.walk(checksPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".sql"))
                    .map(this::getCheck)
                    .map(CheckDto::from)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load checks", e);
        }
    }

    public Check convertCheckDtoToCheck(CheckDto checkDto) {
        if (checkDto == null) {
            throw new NullPointerException("Check DTO is null");
        }

        if (checkDto.getName() == null || checkDto.getName().isEmpty()) {
            return Check.builder()
                    .name("")
                    .error("The Check DTO name is incorrect")
                    .build();
        }

        String filename = checkDto.getName() + checkLoaderUtils.checkFileExtension;
        Path filepath = Paths.get(this.checkLoaderConfiguration.getChecksPath(), filename);
        return getCheck(filepath);
    }

    public Check getCheck(Path filepath) {
        String queryName = checkLoaderUtils.getCheckNameFromPath(filepath);

        try {
            String content = Files.readString(filepath);

            return Check.builder()
                    .name(queryName)
                    .query(content)
                    .build();
        } catch (Exception e) {
            return Check.builder()
                    .name(queryName)
                    .error("Failed to load query")
                    .build();
        }
    }

}
