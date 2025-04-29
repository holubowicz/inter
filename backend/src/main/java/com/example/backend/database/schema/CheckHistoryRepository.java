package com.example.backend.database.schema;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CheckHistoryRepository extends JpaRepository<CheckHistory, Long> {

    List<CheckHistory> findByCheckNameOrderByTimestamp(String checkName);

    Optional<CheckHistory> findTopByCheckNameOrderByTimestampDesc(String checkName);

}
