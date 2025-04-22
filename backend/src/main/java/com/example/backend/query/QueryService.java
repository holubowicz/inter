package com.example.backend.query;

import java.util.List;

public interface QueryService {

    List<QueryDto> getQueries();

    List<QueryResult> runQueries(List<QueryDto> queries);

}
