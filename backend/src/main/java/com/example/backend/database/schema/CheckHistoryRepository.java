package com.example.backend.database.schema;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckHistoryRepository extends JpaRepository<CheckHistory, Long> {

    Optional<CheckHistory> findTopByCheckNameOrderByTimestampDesc(String checkName);

}
