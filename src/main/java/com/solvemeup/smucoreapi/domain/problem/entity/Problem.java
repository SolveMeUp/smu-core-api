package com.solvemeup.smucoreapi.domain.problem.entity;

import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "problems")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Problem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String description;

    @Lob
    private String constraints;

    @Column(nullable = false)
    private int timeLimitMillis;

    @Column(nullable = false)
    private int memoryLimitMegabytes;

    @Column(nullable = false)
    private int difficulty;

    @Column(nullable = false)
    private int submissionCount;

    @Column(nullable = false)
    private int solvedCount;
}
