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
@Table(
        name = "results",
        indexes = {
                @Index(name = "idx_check_name", columnList = "check_name")
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false)
    private Instant timestamp;

    @Column(name = "check_name", nullable = false)
    private String checkName;

    @Column(precision = 38, scale = 16, nullable = false)
    private BigDecimal result;

    @Column(name = "execution_time", nullable = false)
    private Long executionTime;

}
