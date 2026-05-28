package com.solvemeup.smucoreapi.domain.judge.execution.entity;

import com.solvemeup.smucoreapi.global.entity.CreatedTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "execution_case_results",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"execution_id", "case_index"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExecutionCaseResult extends CreatedTimeEntity {

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
    private ExecutionCaseResultStatus status;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String expectedOutput;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String actualOutput;

    private Integer timeUsedMillis;

    private Integer memoryUsedKilobytes;

    public static ExecutionCaseResult create(
            Execution execution,
            int caseIndex,
            ExecutionCaseResultStatus status,
            String actualOutput,
            Integer timeUsedMillis,
            Integer memoryUsedKilobytes
    ) {
        ExecutionCaseResult executionCaseResult = new ExecutionCaseResult();
        executionCaseResult.execution = execution;
        executionCaseResult.caseIndex = caseIndex;
        executionCaseResult.status = status;
        executionCaseResult.actualOutput = actualOutput;
        executionCaseResult.timeUsedMillis = timeUsedMillis;
        executionCaseResult.memoryUsedKilobytes = memoryUsedKilobytes;
        return executionCaseResult;
    }
}
