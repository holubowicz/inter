package com.example.backend.database.schema;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultHistoryRepository extends JpaRepository<ResultHistory, Long> {

    Optional<ResultHistory> findTopByCheckNameOrderByTimestampDesc(String checkName);

}
