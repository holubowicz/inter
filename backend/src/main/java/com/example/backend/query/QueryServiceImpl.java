package com.example.backend.query;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QueryServiceImpl implements QueryService {

    private static final String queryDirectory = "queries";
    private final JdbcTemplate jdbcTemplate;

    public QueryServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<QueryDTO> getQueries() {
        Path queriesPath = Paths.get(queryDirectory).toAbsolutePath();

        if (!Files.exists(queriesPath)) {
            throw new RuntimeException("Query directory does not exist: " + queriesPath);
        }

        try {
            return Files.walk(queriesPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".sql"))
                    .map(this::loadQueryFile)
                    .map(this::convertQueryToQueryDTO)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load queries", e);
        }
    }

    @Override
    public List<List<Map<String, Object>>> runQueries(List<QueryDTO> queries) {
        List<List<Map<String, Object>>> results = new ArrayList<>();

        for (QueryDTO queryDTO : queries) {
            Query query = convertQueryDTOToQuery(queryDTO);
            results.add(runQuery(query));
        }

        return results;
    }

    private Query loadQueryFile(Path filepath) {
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

    private QueryDTO convertQueryToQueryDTO(Query query) {
        return new QueryDTO(query.getName());
    }

    private Query convertQueryDTOToQuery(QueryDTO queryDTO) {
        String filename = queryDTO.getName() + ".sql";
        Path filepath = Paths.get(queryDirectory, filename);

        return loadQueryFile(filepath);
    }

    private List<Map<String, Object>> runQuery(Query query) {
        if (query.getQuery().isEmpty()) return new ArrayList<>();

        return jdbcTemplate.queryForList(query.getQuery());
    }

}
