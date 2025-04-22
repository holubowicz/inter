package com.example.backend.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/queries")
public class QueryController {

    private final QueryService queryService;

    @Autowired
    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    public List<QueryDto> getQueries() {
        return queryService.getQueries();
    }

    @PostMapping("/run")
    public List<QueryResult> runQueries(@RequestBody List<QueryDto> queries) {
        return queryService.runQueries(queries);
    }

}
