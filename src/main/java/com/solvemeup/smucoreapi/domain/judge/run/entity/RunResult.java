package com.solvemeup.smucoreapi.domain.judge.run.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "run_results",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"run_id", "case_index"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RunResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_id", nullable = false)
    private Run run;

    @Column(name = "case_index", nullable = false)
    private int caseIndex;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RunResultStatus status;

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String expectedOutput;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String actualOutput;

    private Integer timeUsedMillis;

    private Integer memoryUsedKilobytes;

    public static RunResult create(
            Run run,
            int caseIndex,
            RunResultStatus status,
            String expectedOutput,
            String actualOutput,
            Integer timeUsedMillis,
            Integer memoryUsedKilobytes
    ) {
        RunResult runResult = new RunResult();
        runResult.run = run;
        runResult.caseIndex = caseIndex;
        runResult.status = status;
        runResult.expectedOutput = expectedOutput;
        runResult.actualOutput = actualOutput;
        runResult.timeUsedMillis = timeUsedMillis;
        runResult.memoryUsedKilobytes = memoryUsedKilobytes;
        return runResult;
    }
}
