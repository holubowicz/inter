package com.example.backend.query;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class QueryServiceMock implements QueryService {

    @Override
    public List<Query> getQueries() {
        return List.of(
                new Query("2000-sum", "SELECT * FROM calculations LIMIT 1", true),
                new Query("2001-sum", "SELECT * FROM calculations LIMIT 2", true),
                new Query("2002-sum", "SELECT * FROM calculations LIMIT 3", false)
        );
    }
}
