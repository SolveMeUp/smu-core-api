package com.solvemeup.smucoreapi.domain.submission.entity;

import com.solvemeup.smucoreapi.global.entity.CreatedTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.solvemeup.smucoreapi.domain.submission.entity.JudgeStatus.*;

@Entity
@Table(name = "judges")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Judge extends CreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private JudgeStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private JudgeResult result;

    private Integer timeUsedMillis;

    private Integer memoryUsedMegabytes;

    public static Judge createJudge(Submission submission) {
        Judge judge = new Judge();
        judge.submission = submission;
        judge.status = PENDING;
        return judge;
    }

    public void markRunning() {
        this.status = RUNNING;
    }

    public void markDone(JudgeResult result,
                         Integer timeUsedMillis,
                         Integer memoryUsedMegabytes) {
        this.status = DONE;
        this.result = result;
        this.timeUsedMillis = timeUsedMillis;
        this.memoryUsedMegabytes = memoryUsedMegabytes;
    }
}
