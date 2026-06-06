package com.solvemeup.smucoreapi.domain.judge.problem.entity;

import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.global.entity.ModifiedTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "problems")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Problem extends ModifiedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String description;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String constraints;

    @Column(nullable = false)
    private int timeLimitMillis;

    @Column(nullable = false)
    private int memoryLimitKilobytes;

    @Column(nullable = false, length = 100)
    private String functionName;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private List<FunctionParameter> parameters;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DataType returnType;

    @Column(nullable = false)
    private int difficulty;

    @Column(nullable = false)
    private int submissionCount;

    @Column(nullable = false)
    private int solvedUserCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProblemStatus status;
}
