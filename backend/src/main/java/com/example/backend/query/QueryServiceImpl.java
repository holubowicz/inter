package com.example.backend.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<QueryResult> runQueries(List<QueryDto> queryDtoList) {
        return queryDtoList
                .stream()
                .map(queryLoader::convertQueryDtoToQuery)
                .map(queryConnector::runQuery)
                .collect(Collectors.toList());
    }
}
