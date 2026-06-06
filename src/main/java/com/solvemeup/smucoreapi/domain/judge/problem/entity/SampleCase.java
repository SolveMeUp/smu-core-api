package com.solvemeup.smucoreapi.domain.judge.problem.entity;

import com.solvemeup.smucoreapi.global.entity.ModifiedTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "sample_cases",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_sample_cases_problem_order",
                        columnNames = {"problem_id", "order_index"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleCase extends ModifiedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Column(nullable = false)
    private int orderIndex;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String arguments;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String expectedOutput;
}
