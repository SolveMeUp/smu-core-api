package com.solvemeup.smucoreapi.domain.judge.execution.entity;

import com.solvemeup.smucoreapi.domain.judge.common.Verdict;
import com.solvemeup.smucoreapi.global.entity.CreatedTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "execution_results",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_execution_results_execution_case",
                        columnNames = {"execution_id", "case_index"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExecutionResult extends CreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "execution_id", nullable = false)
    private Execution execution;

    @Column(nullable = false)
    private int caseIndex;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Verdict verdict;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String arguments;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String expectedOutput;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String actualOutput;

    private Integer timeUsedMillis;

    private Integer memoryUsedKilobytes;

    public static ExecutionResult create(
            Execution execution,
            int caseIndex,
            Verdict verdict,
            String arguments,
            String expectedOutput,
            String actualOutput,
            Integer timeUsedMillis,
            Integer memoryUsedKilobytes
    ) {
        ExecutionResult executionResult = new ExecutionResult();
        executionResult.execution = execution;
        executionResult.caseIndex = caseIndex;
        executionResult.verdict = verdict;
        executionResult.arguments = arguments;
        executionResult.expectedOutput = expectedOutput;
        executionResult.actualOutput = actualOutput;
        executionResult.timeUsedMillis = timeUsedMillis;
        executionResult.memoryUsedKilobytes = memoryUsedKilobytes;
        return executionResult;
    }
}
