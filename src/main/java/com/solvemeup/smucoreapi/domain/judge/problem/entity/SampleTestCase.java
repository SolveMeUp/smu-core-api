package com.solvemeup.smucoreapi.domain.judge.problem.entity;

import com.solvemeup.smucoreapi.global.entity.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "sample_test_cases",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"problem_id", "order_index"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleTestCase extends SoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String arguments;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String expectedOutput;

    @Column(nullable = false)
    private int orderIndex;
}
