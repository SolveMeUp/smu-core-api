package com.solvemeup.smucoreapi.domain.judge.submission.entity;

import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.global.entity.CreatedTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Column(nullable = false, length = 20)
    private Language language;

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String sourceCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubmissionStatus status;

    public static Submission create(User user,
                                    Problem problem,
                                    Language language,
                                    String sourceCode
    ) {
        Submission submission = new Submission();
        submission.user = user;
        submission.problem = problem;
        submission.language = language;
        submission.sourceCode = sourceCode;
        submission.status = PENDING;
        return submission;
    }

    public void markDone() {
        this.status = DONE;
    }
}
