package com.solvemeup.smucoreapi.domain.judge.run.entity;

import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.judge.submission.entity.Language;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.global.entity.CreatedTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.solvemeup.smucoreapi.domain.judge.run.entity.RunStatus.*;

@Entity
@Table(name = "runs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Run extends CreatedTimeEntity {

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
    @Column(nullable = false, length = 20)
    private Language language;

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String sourceCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RunStatus status;

    @Column(nullable = false)
    private int totalCaseCount;

    private LocalDateTime finishedAt;

    public static Run create(User user, Problem problem, Language language, String sourceCode, int totalCaseCount) {
        Run run = new Run();
        run.user = user;
        run.problem = problem;
        run.language = language;
        run.sourceCode = sourceCode;
        run.status = RUNNING;
        run.totalCaseCount = totalCaseCount;
        return run;
    }

    public void markDone() {
        this.status = DONE;
        this.finishedAt = LocalDateTime.now();
    }

    public void markError() {
        this.status = ERROR;
        this.finishedAt = LocalDateTime.now();
    }
}
