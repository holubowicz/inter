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

    public List<QueryDTO> getQueries() {
        Path queriesPath = Paths.get(queryDirectory).toAbsolutePath();

        if (!Files.exists(queriesPath)) {
            throw new RuntimeException("Query directory does not exist: " + queriesPath);
        }

        try {
            return Files.walk(queriesPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".sql"))
                    .map(this::getQuery)
                    .map(this::convertQueryToQueryDTO)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load queries", e);
        }
    }

    public QueryDTO convertQueryToQueryDTO(Query query) {
        return new QueryDTO(query.getName());
    }

    public Query convertQueryDTOToQuery(QueryDTO queryDTO) {
        String filename = queryDTO.getName() + ".sql";
        Path filepath = Paths.get(queryDirectory, filename);
        return getQuery(filepath);
    }

    private Query getQuery(Path filepath) {
        String queryName = getQueryNameFromPath(filepath);
        try {
            String content = Files.readString(filepath);
            return new Query(queryName, content, true);
        } catch (IOException e) {
            return new Query(queryName, "", false);
        }
    }

    private String getQueryNameFromPath(Path filepath) {
        String filename = filepath.getFileName().toString();
        return filename.substring(0, filename.lastIndexOf(".sql"));
    }

}
