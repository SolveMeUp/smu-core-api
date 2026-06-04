package com.solvemeup.smucoreapi.domain.judge.execution.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "execution_results",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"execution_id", "case_index"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExecutionResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "execution_id", nullable = false)
    private Execution execution;

    @Column(name = "case_index", nullable = false)
    private int caseIndex;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ExecutionResultStatus status;

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String expectedOutput;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String actualOutput;

    private Integer timeUsedMillis;

    private Integer memoryUsedKilobytes;

    public static ExecutionResult create(
            Execution execution,
            int caseIndex,
            ExecutionResultStatus status,
            String expectedOutput,
            String actualOutput,
            Integer timeUsedMillis,
            Integer memoryUsedKilobytes
    ) {
        ExecutionResult executionResult = new ExecutionResult();
        executionResult.execution = execution;
        executionResult.caseIndex = caseIndex;
        executionResult.status = status;
        executionResult.expectedOutput = expectedOutput;
        executionResult.actualOutput = actualOutput;
        executionResult.timeUsedMillis = timeUsedMillis;
        executionResult.memoryUsedKilobytes = memoryUsedKilobytes;
        return executionResult;
    }
}
