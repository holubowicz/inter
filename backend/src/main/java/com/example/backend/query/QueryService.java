package com.example.backend.query;

import java.util.List;
import java.util.Map;

public interface QueryService {

    List<QueryDTO> getQueries();

    List<List<Map<String, Object>>> runQueries(List<QueryDTO> queries);

}
