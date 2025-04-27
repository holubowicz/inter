package com.example.backend.database.schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "results")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant timestamp;

    @Column(name = "check_name", nullable = false, updatable = false)
    private String checkName;

    @Column(precision = 38, scale = 16, nullable = false, updatable = false)
    private BigDecimal result;

}
