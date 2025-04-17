package com.example.backend.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QueryServiceImpl implements QueryService {

    private final QueryLoader queryLoader;
    private final QueryConnector queryConnector;

    @Autowired
    public QueryServiceImpl(QueryLoader queryLoader, QueryConnector queryConnector) {
        this.queryLoader = queryLoader;
        this.queryConnector = queryConnector;
    }

    @Override
    public List<QueryDto> getQueries() {
        return queryLoader.getQueries();
    }

    @Override
    public List<List<Map<String, Object>>> runQueries(List<QueryDto> queries) {
        List<List<Map<String, Object>>> results = new ArrayList<>();

        for (QueryDto queryDto : queries) {
            Query query = queryLoader.convertQueryDtoToQuery(queryDto);
            results.add(queryConnector.runQuery(query));
        }

        return results;
    }
}
