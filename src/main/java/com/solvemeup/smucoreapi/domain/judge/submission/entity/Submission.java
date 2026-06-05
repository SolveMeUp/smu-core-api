package com.solvemeup.smucoreapi.domain.judge.submission.entity;

import com.solvemeup.smucoreapi.domain.judge.common.Language;
import com.solvemeup.smucoreapi.domain.judge.common.Verdict;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.global.entity.CreatedTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.solvemeup.smucoreapi.domain.judge.submission.entity.SubmissionStatus.*;

@Entity
@Table(name = "submissions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Submission extends CreatedTimeEntity {

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmissionStatus status;

    @Enumerated(EnumType.STRING)
    private Verdict verdict;

    private Integer failedCaseIndex;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String arguments;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String expectedOutput;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String actualOutput;

    private Integer timeUsedMillis;

    private Integer memoryUsedKilobytes;

    private LocalDateTime finishedAt;

    public static Submission create(User user, Problem problem, Language language, String sourceCode) {
        Submission submission = new Submission();
        submission.user = user;
        submission.problem = problem;
        submission.language = language;
        submission.sourceCode = sourceCode;
        submission.status = PENDING;
        return submission;
    }

    public void markDone(Verdict verdict,
                         Integer failedCaseIndex,
                         String arguments,
                         String expectedOutput,
                         String actualOutput,
                         Integer timeUsedMillis,
                         Integer memoryUsedKilobytes) {
        if (this.status == DONE) {
            return;
        }
        this.status = DONE;
        this.verdict = verdict;
        this.failedCaseIndex = failedCaseIndex;
        this.arguments = arguments;
        this.expectedOutput = expectedOutput;
        this.actualOutput = actualOutput;
        this.timeUsedMillis = timeUsedMillis;
        this.memoryUsedKilobytes = memoryUsedKilobytes;
        this.finishedAt = LocalDateTime.now();
    }
}
