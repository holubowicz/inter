package com.example.backend.query;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class QueryLoader {

    private static final String queryDirectory = "queries";

    public List<QueryDto> getQueries() {
        Path queriesPath = Paths.get(queryDirectory).toAbsolutePath();

        if (!Files.exists(queriesPath)) {
            throw new RuntimeException("Query directory does not exist: " + queriesPath);
        }

        try {
            return Files.walk(queriesPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".sql"))
                    .map(this::getQuery)
                    .map(QueryDto::from)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load queries", e);
        }
    }

    private Query getQuery(Path filepath) {
        String queryName = getQueryNameFromPath(filepath);
        try {
            String content = Files.readString(filepath);
            return Query.builder().name(queryName).query(content).build();
        } catch (IOException e) {
            return Query.builder().name(queryName).build();
        }
    }

    public Query convertQueryDtoToQuery(QueryDto queryDto) {
        String filename = queryDto.getName() + ".sql";
        Path filepath = Paths.get(queryDirectory, filename);
        return getQuery(filepath);
    }

    private String getQueryNameFromPath(Path filepath) {
        String filename = filepath.getFileName().toString();
        return filename.substring(0, filename.lastIndexOf(".sql"));
    }

}
