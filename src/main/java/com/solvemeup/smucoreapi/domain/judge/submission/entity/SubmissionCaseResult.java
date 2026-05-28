package com.solvemeup.smucoreapi.domain.judge.submission.entity;

import com.solvemeup.smucoreapi.global.entity.CreatedTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "submission_case_results")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubmissionCaseResult extends CreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SubmissionCaseResultStatus result;

    private Integer timeUsedMillis;

    private Integer memoryUsedKilobytes;

    public static SubmissionCaseResult create(Submission submission) {
        SubmissionCaseResult submissionCaseResult = new SubmissionCaseResult();
        submissionCaseResult.submission = submission;
        return submissionCaseResult;
    }

    public void markDone(SubmissionCaseResultStatus result,
                         Integer timeUsedMillis,
                         Integer memoryUsedKilobytes) {
        this.result = result;
        this.timeUsedMillis = timeUsedMillis;
        this.memoryUsedKilobytes = memoryUsedKilobytes;
    }
}
