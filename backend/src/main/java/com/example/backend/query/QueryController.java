package com.example.backend.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/query")
public class QueryController {

    private final QueryService queryService;

    @Autowired
    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    public List<QueryDTO> getQueries() {
        return queryService.getQueries();
    }

    @PostMapping
    public List<List<Map<String, Object>>> runQueries(@RequestBody List<QueryDTO> queries) {
        return queryService.runQueries(queries);
    }

}
