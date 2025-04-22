package com.example.backend.database.internal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant timestamp;

    @Column(name = "check_name", nullable = false, updatable = false)
    private String checkName;

    @Column(columnDefinition = "NUMERIC(38, 16)", nullable = false, updatable = false)
    private BigDecimal result;

}
