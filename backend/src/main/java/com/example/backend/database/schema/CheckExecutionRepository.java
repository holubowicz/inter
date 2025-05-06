package com.example.backend.database.schema;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CheckExecutionRepository extends JpaRepository<CheckExecution, Long> {

    List<CheckExecution> findByCheckNameOrderByTimestamp(String checkName);

    Optional<CheckExecution> findTopByCheckNameOrderByTimestampDesc(String checkName);

}
