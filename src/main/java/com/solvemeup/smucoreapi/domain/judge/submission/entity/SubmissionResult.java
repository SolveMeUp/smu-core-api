package com.solvemeup.smucoreapi.domain.judge.submission.entity;

import com.solvemeup.smucoreapi.global.entity.CreatedTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.solvemeup.smucoreapi.domain.judge.submission.entity.SubmissionResultStatus.*;

@Entity
@Table(name = "submission_result")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubmissionResult extends CreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubmissionResultStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SubmissionResultResult result;

    private Integer timeUsedMillis;

    private Integer memoryUsedKilobytes;

    public static SubmissionResult create(Submission submission) {
        SubmissionResult submissionResult = new SubmissionResult();
        submissionResult.submission = submission;
        submissionResult.status = PENDING;
        return submissionResult;
    }

    public void markDone(SubmissionResultResult result,
                         Integer timeUsedMillis,
                         Integer memoryUsedKilobytes) {
        this.status = DONE;
        this.result = result;
        this.timeUsedMillis = timeUsedMillis;
        this.memoryUsedKilobytes = memoryUsedKilobytes;
    }
}
