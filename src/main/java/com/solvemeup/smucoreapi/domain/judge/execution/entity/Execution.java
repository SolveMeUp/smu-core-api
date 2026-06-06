package com.solvemeup.smucoreapi.domain.judge.execution.entity;

import com.solvemeup.smucoreapi.domain.judge.common.Language;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.global.entity.CreatedTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionStatus.DONE;
import static com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionStatus.RUNNING;

@Entity
@Table(name = "executions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Execution extends CreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language language;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String sourceCode;

    @Column(nullable = false)
    private int totalCaseCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExecutionStatus status;

    private LocalDateTime finishedAt;

    public static Execution create(User user, Problem problem, Language language, String sourceCode, int totalCaseCount) {
        Execution execution = new Execution();
        execution.user = user;
        execution.problem = problem;
        execution.language = language;
        execution.sourceCode = sourceCode;
        execution.totalCaseCount = totalCaseCount;
        execution.status = RUNNING;
        return execution;
    }

    public void markDone() {
        if (this.status == DONE) {
            return;
        }
        this.status = DONE;
        this.finishedAt = LocalDateTime.now();
    }
}
